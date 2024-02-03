package com.example.calendarapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ClassFragment extends Fragment{

    private ArrayList<Classes> classes;
    private ArrayAdapter<Classes> adapter;

    private EditText eiditCourseName, eiditCourseTime, eiditCourseInstructor;
    private Button btnClassAdd,btnClassEdit;
    private ListView listClass;
    private String item;
    private int indexVal;
    private Classes classval;


    public ClassFragment() {
        //Constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class, container, false);

        classes = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, classes);

        eiditCourseName = view.findViewById(R.id.eiditCourseName);
        eiditCourseTime = view.findViewById(R.id.eiditCourseTime);
        eiditCourseInstructor = view.findViewById(R.id.eiditCourseInstructor);
        btnClassAdd = view.findViewById(R.id.btnClassAdd);
        btnClassEdit = view.findViewById(R.id.btnClassEdit);
        listClass = view.findViewById(R.id.listClass);
        listClass.setAdapter(adapter);


        //add data
        btnClassAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClass();
            }
        });

        //Update data
        btnClassEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               updateClass();;
            }
        });

        //getting index when click on listview
        listClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString()+"has been selected";
                indexVal = position;
                Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
            }
        });

        //Delete listview class when double click
        listClass.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString()+"has been deleted";
                Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
                indexVal = position;
                delete();
                adapter.notifyDataSetChanged();
                indexVal =0; //reset
                return true;
            }
        });



        return view;
    }

    private void addClass() {
        String classes = eiditCourseName.getText().toString();
        String time = eiditCourseTime.getText().toString();
        String instructor = eiditCourseInstructor.getText().toString();



        if (!classes.isEmpty() && !time.isEmpty() && !instructor.isEmpty()) {
            Classes newClass = new Classes(classes,time,instructor);
            this.classes.add(newClass);
            adapter.notifyDataSetChanged();

            // Clear input fields
            eiditCourseName.getText().clear();
            eiditCourseTime.getText().clear();
            eiditCourseInstructor.getText().clear();
        }
    }

    //Update class
    private void updateClass(){
        String classes = eiditCourseName.getText().toString();
        String time = eiditCourseTime.getText().toString();
        String instructor = eiditCourseInstructor.getText().toString();
        Classes newClass = new Classes(classes,time,instructor);

        this.classes.set(indexVal,newClass);
        adapter.notifyDataSetChanged();

        eiditCourseName.getText().clear();
        eiditCourseTime.getText().clear();
        eiditCourseInstructor.getText().clear();
        indexVal = 0; //reset index
    }

    //Delete
    private void delete(){
        this.classes.remove(indexVal);
    }


    private static class Classes {
        private String classes;
        private String time;
        private String instructor;

        Classes(String classes, String time, String instructor) {
            this.classes = classes;
            this.time = time;
            this.instructor = instructor;
        }
        Classes(){}

        @NonNull
        @Override
        public String toString() {
            return "Classes: " + classes + ", Time: " + time + ", Instructor: " + instructor;
        }
    }

}