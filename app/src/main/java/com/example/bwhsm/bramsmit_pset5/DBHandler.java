package com.example.bwhsm.bramsmit_pset5;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tasks.db";
    public static final String TABLE_TASKS = "tasks";
    public static final String TABLE_LISTS = "lists";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "title";
    public static final String COLUMN_LIST_ID = "list_id";
    public static final String COLUMN_COMPLETED = "completed";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = " CREATE TABLE " + TABLE_TASKS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_COMPLETED + " INTEGER DEFAULT 0, " +
                COLUMN_LIST_ID + " INTEGER" +
                ");";
        db.execSQL(query);
        query =  " CREATE TABLE " + TABLE_LISTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT" + ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTS);
        onCreate(db);
    }

    // Add new task to database
    public void addItem(Task task) {
        ContentValues values = new ContentValues();
        if (!task.getName().equals(null)) {
            values.put(COLUMN_NAME, task.getName());
            int completed;
            if (!task.getFinished()) {
                completed = 0;
            } else {
                completed = 1;
            }
            values.put(COLUMN_COMPLETED, completed);
            values.put(COLUMN_LIST_ID, task.getListId());
            SQLiteDatabase db = getWritableDatabase();
            db.insert(TABLE_TASKS, null, values);
            db.close();
        }
    }

    public void addItem(TaskList list) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, list.getTitle());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_LISTS, null, values);
        db.close();
    }

    // Delete item from database
    public void deleteItems(ArrayList<Task> toBeDeleted) {
        SQLiteDatabase db = getWritableDatabase();
        for (int i=0;i<toBeDeleted.size();i++) {
            Task currentTask = toBeDeleted.get(i);
            db.delete(TABLE_TASKS, " " + COLUMN_ID + " = ? ", new String[] {String.valueOf(currentTask.getId())});
        }
        db.close();
    }

    public void deleteList(TaskList list) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_LISTS, " " + COLUMN_ID + " =? ", new String[] {String.valueOf(list.getId())});
        db.close();
    }


    public void updateItem(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, task.getName());
        int completed;
        if (!task.getFinished()) {
            completed = 0;
        } else {
            completed = 1;
        }
        values.put(COLUMN_COMPLETED, completed);
        db.update(TABLE_TASKS, values, COLUMN_ID + " = ? ", new String[] { String.valueOf(task.getId())});
    }
    // return database as Array list
    public ArrayList<TaskList> databaseToArray() {
        ArrayList<TaskList> taskLists = new ArrayList<TaskList>();
        SQLiteDatabase db = getReadableDatabase();
        String query = " SELECT * FROM " + TABLE_LISTS + " WHERE 1 ";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            TaskList newList= new TaskList();
            newList.setTitle(c.getString(c.getColumnIndex(COLUMN_NAME)));
            newList.setId(c.getInt(c.getColumnIndex(COLUMN_ID)));
            taskLists.add(newList);
            c.moveToNext();
        }
        db.close();
        query = " SELECT * FROM " + TABLE_TASKS + " WHERE 1 ";
        db = getReadableDatabase();
        c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            Task newTask = new Task();
            if (c.getString(c.getColumnIndex(COLUMN_NAME)) != null) {
                newTask.setName(c.getString(c.getColumnIndex(COLUMN_NAME)));
            }

            newTask.setId(c.getInt(c.getColumnIndex(COLUMN_ID)));
            int completed = c.getInt(c.getColumnIndex(COLUMN_COMPLETED));
            if (completed == 0) {
                newTask.setFinished(false);
            } else {
                newTask.setFinished(true);
            }
            newTask.setListId(c.getInt(c.getColumnIndex(COLUMN_LIST_ID)));
            for (int i=0;i<taskLists.size();i++) {
                if (taskLists.get(i).getId() == newTask.getListId()) {
                    taskLists.get(i).addTask(newTask);
                }
            }
            c.moveToNext();
        }
        db.close();
        return taskLists;
    }

    public void clearDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TASKS);
        db.execSQL("DELETE FROM " + TABLE_LISTS);
    }
}