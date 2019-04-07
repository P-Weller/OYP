package com.example.oyp;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.oyp.Fragments.ActivityFragment;
import com.example.oyp.Fragments.CreateTaskFragment;
import com.example.oyp.Fragments.MoreFragment;
import com.example.oyp.Fragments.TasksFragment;
import com.example.oyp.Fragments.UsersFragment;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomNav = findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new TasksFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.navigation_tasks:
                            selectedFragment = new TasksFragment();
                            break;
                        case R.id.navigation_activity:
                            selectedFragment = new ActivityFragment();
                            break;
                        case R.id.navigation_createTask:
                            selectedFragment = new CreateTaskFragment();
                            break;
                        case R.id.navigation_more:
                            selectedFragment = new MoreFragment();
                            break;
                        case R.id.navigation_users:
                            selectedFragment = new UsersFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }

            };
}





