package com.example.bwhsm.bramsmit_pset5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<TaskList> listArray;
    int currentListIndex;
    ListView lvTasks;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        dbHandler = DBHandler.getInstance(this);

//        dbHandler.clearDatabase();
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putInt("currentListIndex", 0);
//        editor.commit();

        getListArray();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // TODO add OnClick events for the fab_menu
        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.speedDial);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_addItem) {
                    goToInputActivity("task");
                } else {
                    if (menuItem.getItemId() == R.id.action_addList) {
                        // TODO set focus on new list
                        goToInputActivity("list");
                    }
                }
                return true;
            }
        });

        // Set the first list as default for now TODO Save which list the user had open on previous run
        if (listArray.size() != 0) {
            currentListIndex = sharedPref.getInt("currentListIndex", 0);

        } else {
            TaskList exampleList = new TaskList("Example List");
            dbHandler.addItem(exampleList);
            getListArray();
            currentListIndex = 0;

            Task exampleTask = new Task("Example Task");
            exampleTask.setListId(listArray.get(currentListIndex).getId());
            dbHandler.addItem(exampleTask);
            getListArray();
        }

//        editor = sharedPref.edit();
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putInt("currentListIndex", currentListIndex);
//        editor.apply();

        setNavigationMenu();
        loadListView();


    }

    private void goToInputActivity(String setting) {
        int listId = listArray.get(currentListIndex).getId();
        Intent inputIntent = new Intent(this, InputActivity.class);
        inputIntent.putExtra("listId", listId);
        inputIntent.putExtra("setting", setting);
        this.startActivity(inputIntent);
        finish();
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


    // Load the listview with the currentListIndex from listArray.
    private void loadListView() {

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("currentListIndex", currentListIndex);
        editor.apply();

        ArrayAdapter arrayAdapter = new CustomAdapter(this,listArray.get(currentListIndex).getTaskList());
        lvTasks = (ListView) findViewById(R.id.taskList);
        lvTasks.setAdapter(arrayAdapter);
        lvTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = (Task) parent.getItemAtPosition(position);
                dbHandler.deleteTask(task);
                getListArray();
                loadListView();
                return true;
            }
        });
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        // TODO Change to dynamic for loop where the currentListIndex is set equal to the ItemId of the MenuItem.
        currentListIndex = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        loadListView();
        return true;
    }
}
