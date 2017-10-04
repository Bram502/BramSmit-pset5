package com.example.bwhsm.bramsmit_pset5;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by bwhsm on 20-9-2017.
 */

public class CustomAdapter extends ArrayAdapter<Task> {

    MainActivity activity;

    public CustomAdapter(MainActivity activity, @NonNull Context context, ArrayList<Task> taskList) {
        super(context, R.layout.custom_row,taskList);
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_row, parent, false);

        final Task task = getItem(position);

        final MainActivity mainActivity = (MainActivity) activity;
        CheckBox checkBox = (CheckBox) customView.findViewById(R.id.checkBox);
        TextView taskText = (TextView) customView.findViewById(R.id.taskText);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.itemChecked(task);
            }
        });

        taskText.setText(task.getName());
        checkBox.setChecked(task.getFinished());
        return customView;
    }
}
