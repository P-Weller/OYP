package com.example.oyp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.oyp.ClosedTasksAdapter;
import com.example.oyp.R;

public class ClosedTasksFragment extends Fragment {

    public ClosedTasksFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_closed_task, container, false);

        int[] tImage = {R.drawable.living, R.drawable.ic_person_add_black_24dp, R.drawable.ic_color_lens_black_32dp, R.drawable.ic_loop_black_32dp,
                R.drawable.ic_color_lens_black_32dp, R.drawable.ic_attach_money_black_32dp, R.drawable.ic_notifications_black_24dp};

        String[] tName = {"Clean", "Clean more", "Clean!!", "Ironing", "Clean kitchen", "clean bathroom", "maw the lawn"};

        ListView closedTListView = (ListView) view.findViewById(R.id.ctasksListView);

        ClosedTasksAdapter taskAdapter = new ClosedTasksAdapter(this.getContext(),tImage, tName);
        closedTListView.setAdapter(taskAdapter);
        return view;

    }
}

