package com.example.oyp;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemReselectedListener {

    // ListView variablen erstellen
        ListView userListView;
        String[] uNames;
        String[] uPoints;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new TasksFragment());

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setSelectedItemId(R.id.navigation);

        /*
         * Test Kommentar

         */


        // ListView UserAnsicht

        Resources res = getResources();
        userListView = (ListView) findViewById(R.id.userListView);
        uNames = res.getStringArray(R.array.uNames);
        uPoints = res.getStringArray(R.array.uPoints);

        uNamesAdapter uNamesAdapter = new uNamesAdapter(this, uNames, uPoints);
        userListView.setAdapter(uNamesAdapter);


        // userListView.setAdapter(new ArrayAdapter<String>(this, R.layout.user_list_view_detail, uNames));





    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();


            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch(item.getItemId()) {

            case R.id.navigation_tasks:
                fragment = new TasksFragment();
                break;
            case R.id.navigation_activity:
                fragment = new ActivityFragment();
                break;
            case R.id.navigation_more:
                fragment = new MoreFragment();
                break;
            case R.id.navigation_users:
                fragment = new UsersFragment();
                break;

        }

        return loadFragment(fragment);
    }


    public void onNavigationItemReselected(@NonNull MenuItem item) {
    }
}