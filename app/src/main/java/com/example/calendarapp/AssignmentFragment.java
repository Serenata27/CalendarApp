package com.example.calendarapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AssignmentFragment extends Fragment{

    private ArrayList<Assignment> assignments;
    private ArrayAdapter<Assignment> adapter;

    private EditText assignTitle, assignDue, assignClass;
    private Button btnAdd, btnEdit;
    private ListView listAssign;
    private String item;
    private int indexVal;
    private Assignment assignmentVal;

    public AssignmentFragment() {
        //Constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assignment, container, false);
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, assignments);


        assignTitle = view.findViewById(R.id.asignmentTitle);
        assignDue = view.findViewById(R.id.asignmentDue);
        assignClass = view.findViewById(R.id.asignmentClass);
        btnAdd = view.findViewById(R.id.asignmentadd);
        btnEdit = view.findViewById(R.id.asignmentEdit);
        listAssign = view.findViewById(R.id.asignmentList);
        listAssign.setAdapter(adapter);
        //add data
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        //Update data
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();;
            }
        });

        //getting index when click on listview
        listAssign.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString()+"has been selected";
                indexVal = position;
                Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
            }
        });

        //Delete listview class when double click
        listAssign.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

    private void add() {
        String classes = assignTitle.getText().toString();
        String time = assignDue.getText().toString();
        String instructor = assignClass.getText().toString();



        if (!classes.isEmpty() && !time.isEmpty() && !instructor.isEmpty()) {
            Assignment newClass = new Assignment(classes,time,instructor);
            this.assignments.add(newClass);
            adapter.notifyDataSetChanged();

            // Clear input fields
            assignTitle.getText().clear();
            assignDue.getText().clear();
            assignClass.getText().clear();
        }
        saveDate();
    }

    //Update class
    private void update(){
        String classes = assignTitle.getText().toString();
        String time = assignDue.getText().toString();
        String instructor = assignClass.getText().toString();
        Assignment newClass = new Assignment(classes,time,instructor);

        this.assignments.set(indexVal,newClass);
        adapter.notifyDataSetChanged();

        assignTitle.getText().clear();
        assignDue.getText().clear();
        assignClass.getText().clear();
        indexVal = 0; //reset index
        saveDate();

    }

    //Delete
    private void delete(){
        this.assignments.remove(indexVal);
        saveDate();
    }
    //save data
    private void saveDate(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("classShare1", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(assignments);
        editor.putString("list1",json);
        editor.apply();
    }
    private void loadData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("classShare1", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list1",null);
        Type type = new TypeToken<ArrayList<Assignment>>() {}.getType();
        assignments = gson.fromJson(json, type);

        if(assignments == null){
            assignments = new ArrayList<>();
        }
    }

    private static class Assignment {
        private String classes;
        private String time;
        private String instructor;

        Assignment(String classes, String time, String instructor) {
            this.classes = classes;
            this.time = time;
            this.instructor = instructor;
        }
        Assignment(){}

        @NonNull
        @Override
        public String toString() {
            return "Assignment: " + classes + ", Due date: " + time + ", Class: " + instructor;
        }

    }

}