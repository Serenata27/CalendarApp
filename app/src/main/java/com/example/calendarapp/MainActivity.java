package com.example.calendarapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.calendarapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    EditText eiditCourseName,eiditCourseTime,eiditCourseInstructor;
    Button btnClassAdd;
    ListView listClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //FloatingActionButton fab = findViewById(R.id.add_exam);
        //fab.setOnClickListener(view -> openExamAdder());


        // this is for bottom navigation view
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

        //this is for fragment class
        eiditCourseName = (EditText) findViewById(R.id.eiditCourseName);
        eiditCourseTime = (EditText) findViewById(R.id.eiditCourseTime);
        eiditCourseInstructor = (EditText) findViewById(R.id.eiditCourseInstructor);
        btnClassAdd = (Button) findViewById(R.id.btnClassAdd);
        listClass = (ListView) findViewById(R.id.listClass);
        //ArrayList<String> classes = new ArrayList<>();
        //ArrayAdapter<String> adapterClasses = new ArrayAdapter<String>(this,R.layout.fragment_class,R.id.listClass,classes);
        listClass.setAdapter(adapterClasses);
        btnClassAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseName = eiditCourseName.getText().toString();
                String courseTime = eiditCourseTime.getText().toString();
                String courseInstructor = eiditCourseInstructor.getText().toString();
                classes.add("Course: "+courseName+" Time: "+courseTime+" Instructor: "+courseInstructor);
            }

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