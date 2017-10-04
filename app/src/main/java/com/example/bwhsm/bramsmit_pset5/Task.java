package com.example.bwhsm.bramsmit_pset5;

/**
 * Created by bwhsm on 3-10-2017.
 */

public class Task {
    private int id;
    private String name;
    private Boolean finished = false;
    private int listId;

    public Task() {
    }

    public Task(String name) {
        this.name = name;
    }

    public int getId() {
        return id;

    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}
