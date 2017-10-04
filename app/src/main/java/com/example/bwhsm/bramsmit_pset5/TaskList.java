package com.example.bwhsm.bramsmit_pset5;

import java.util.ArrayList;

/**
 * Created by bwhsm on 3-10-2017.
 */

public class TaskList {
    private ArrayList<Task> taskList;
    private String title;
    private int id;

    public TaskList() {
        taskList = new ArrayList<Task>();
    }

    public TaskList(String title) {
        this.title = title;
        taskList = new ArrayList<Task>();
    }

    public TaskList(ArrayList<Task> taskList, String title) {
        this.taskList = taskList;

        this.title = title;
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addTask(Task task) {
        taskList.add(task);
    }

    public void removeTask(Task task) {
        taskList.remove(task);
    }
}
