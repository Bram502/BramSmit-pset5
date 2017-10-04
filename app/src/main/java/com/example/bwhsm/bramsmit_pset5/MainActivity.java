package com.example.bwhsm.bramsmit_pset5;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<TaskList> listArray;
    int currentList;
    ListView lvTasks;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO Make DBhandler singleton
        dbHandler = new DBHandler(this,null,null,1);
        dbHandler.clearDatabase();
        getListArray();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // TODO add OnClick events for the fab_menu

        setNavigationMenu();

        // Set the first list as default for now TODO Save which list the user had open on previous run
        if (listArray.size() != 0) {
            currentList = 0;
            loadListView();
        } else {
            TaskList exampleList = new TaskList("Example List");
            dbHandler.addItem(exampleList);
            getListArray();
            currentList = 0;
            Task exampleTask = new Task("Example Task");
            exampleTask.setListId(listArray.get(currentList).getId());
            dbHandler.addItem(exampleTask);
            getListArray();
            setNavigationMenu();
            loadListView();
        }

    }

    // Initialize the navigation drawer with the lists from the listArray
    private void setNavigationMenu() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        final Menu menu = navigationView.getMenu();
        Drawable icon = ContextCompat.getDrawable(this, R.drawable.ic_view_list_black_24dp);
        menu.clear();
        for (int i=0; i<listArray.size();i++) {
            menu.add(0,i,0,listArray.get(i).getTitle()).setIcon(icon);
        }
        navigationView.setNavigationItemSelectedListener(this);
    }

    // Retrieve the listArray from the database
    private void getListArray() {
        listArray = dbHandler.databaseToArray();
    }

    // Load the listview with the currentList from listArray.
    private void loadListView() {
        if (listArray.get(currentList).getTaskList() != null) {
            ArrayAdapter arrayAdapter = new CustomAdapter(this,getApplicationContext(), listArray.get(currentList).getTaskList());
            lvTasks = (ListView) findViewById(R.id.taskList);
            lvTasks.setAdapter(arrayAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // TODO update item in database and reload everything.
    public void itemChecked(Task task) {
        if (task.getFinished()) {
            task.setFinished(false);
        } else {
            task.setFinished(true);
        }
        dbHandler.updateItem(task);
        getListArray();
        loadListView();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        // TODO Change to dynamic for loop where the currentList is set equal to the ItemId of the MenuItem.
        switch (item.getItemId()) {
            case 0:
                currentList = 0;
                break;
            case 1:
                break;
            case 2:
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
