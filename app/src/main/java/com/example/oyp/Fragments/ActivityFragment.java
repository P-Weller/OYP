package com.example.oyp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.oyp.ActivityAdapter;
import com.example.oyp.R;

public class ActivityFragment extends Fragment {

    public ActivityFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);

        int[] aImage = {R.drawable.living, R.drawable.ic_person_add_black_24dp, R.drawable.ic_color_lens_black_32dp, R.drawable.ic_loop_black_32dp,
                R.drawable.ic_color_lens_black_32dp, R.drawable.ic_attach_money_black_32dp, R.drawable.ic_notifications_black_24dp};
        String[] aName = {"Activity1", "Cleaning", "Ironing", "Ironing2", "Clean kitchen", "clean bathroom", "maw the lawn"};

        ListView activityListView = (ListView) view.findViewById(R.id.activityListView);

        ActivityAdapter activityAdapter = new ActivityAdapter(this.getContext(),aImage, aName);
        activityListView.setAdapter(activityAdapter);
        return view;


    }
}

