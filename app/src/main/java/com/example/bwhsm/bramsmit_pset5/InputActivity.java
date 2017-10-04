package com.example.bwhsm.bramsmit_pset5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import java.io.Serializable;

public class InputActivity extends AppCompatActivity {

    EditText userInput;
    String input;
    DBHandler dbHandler;
    int listId;
    String setting;
    TaskList list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Intent intent = getIntent();
        listId = intent.getIntExtra("listId",0);
        setting = intent.getStringExtra("setting");

        dbHandler = DBHandler.getInstance(this);

        userInput = (EditText) findViewById(R.id.userInput);
        userInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if (setting.equals("task")) {
                                newTaskEntry();
                            } else if (setting.equals("list")) {
                                newListEntry();
                            }
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });


        findViewById(R.id.enterButton).setOnClickListener(new enterClicked());
    }

    private void newListEntry() {
        input = userInput.getText().toString();
        list = new TaskList(input);
        dbHandler.addItem(list);
        goToMain();
    }


    private void newTaskEntry() {
        input = userInput.getText().toString();
        Task task = new Task(input);
        task.setListId(listId);
        dbHandler.addItem(task);
        goToMain();
    }

    private class enterClicked implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (setting.equals("task")) {
                newTaskEntry();
            } else if (setting.equals("list")) {
                newListEntry();
            }
        }
    }

    @Override
    public void onBackPressed() {
        goToMain();
    }

    private void goToMain() {
        Intent mainIntent = new Intent(this,MainActivity.class);
        this.startActivity(mainIntent);
        finish();
    }
}
