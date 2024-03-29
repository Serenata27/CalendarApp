package com.example.calendarapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private Fragment MyFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        loadFragment(new HomeFragment());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                if (item.getItemId() == R.id.navhome) {
                    fragment = new HomeFragment();
                } else if (item.getItemId() == R.id.navclass) {
                    fragment = new ClassFragment();
                } else if (item.getItemId() == R.id.navassignment) {
                    fragment = new AssignmentFragment();
                } else if (item.getItemId() == R.id.navexam) {
                    fragment = new ExamFragment();
                } else if (item.getItemId() == R.id.navtodo) {
                    fragment = new TodoFragment();
                }

                return loadFragment(fragment);
            }
        });

        //Restore the fragment's instance
        if (savedInstanceState != null) {
            MyFragment = getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");
        }

    }


    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }



}

