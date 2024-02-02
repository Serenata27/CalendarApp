package com.example.calendarapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.calendarapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //FloatingActionButton fab = findViewById(R.id.add_exam);
        //fab.setOnClickListener(view -> openExamAdder());

        binding.bottomNavigation.setBackground(null);
        binding.bottomNavigation.setOnItemSelectedListener(item -> {

           if( item.getItemId() == R.id.navhome ){
               replaceFragment(new HomeFragment());
           } else if (item.getItemId() == R.id.navclass) {
               replaceFragment(new ClassFragment());
           } else if (item.getItemId() == R.id.navassignment) {
               replaceFragment(new AssignmentFragment());
           } else if (item.getItemId() == R.id.navexam) {
               replaceFragment(new ExamFragment());
           } else if (item.getItemId() == R.id.navtodo) {
               replaceFragment(new TodoFragment());
           }
           return true;
        });
    }

    private void openExamAdder() {
        Intent intent = new Intent(this, ExamAdder.class);
        startActivity(intent);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }



}