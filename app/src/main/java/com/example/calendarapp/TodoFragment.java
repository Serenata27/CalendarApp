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

public class TodoFragment extends Fragment{

    private ArrayList<Classes> classes;
    private ArrayAdapter<Classes> adapter;

    private EditText eiditCourseName, eiditCourseTime, eiditCourseInstructor;
    private Button btnClassAdd,btnClassEdit;
    private ListView listClass;
    private String item;
    private int indexVal;
    private Classes classval;

    public TodoFragment() {
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
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, classes);
        eiditCourseName = view.findViewById(R.id.todoText);
        btnClassAdd = view.findViewById(R.id.todoAdd);
        btnClassEdit = view.findViewById(R.id.todoEdit);
        listClass = view.findViewById(R.id.todoListView);
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



        if (!classes.isEmpty()) {
            Classes newClass = new Classes(classes);
            this.classes.add(newClass);
            adapter.notifyDataSetChanged();

            // Clear input fields
            eiditCourseName.getText().clear();
        }
        saveDate();
    }

    //Update class
    private void updateClass(){
        String classes = eiditCourseName.getText().toString();
        Classes newClass = new Classes(classes);

        this.classes.set(indexVal,newClass);
        adapter.notifyDataSetChanged();

        eiditCourseName.getText().clear();
        indexVal = 0; //reset index
        saveDate();

    }

    //Delete
    private void delete(){
        this.classes.remove(indexVal);
        saveDate();
    }
    //save data
    private void saveDate(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("classShare2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(classes);
        editor.putString("list2",json);
        editor.apply();
    }
    private void loadData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("classShare2", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list2",null);
        Type type = new TypeToken<ArrayList<Classes>>() {}.getType();
        classes = gson.fromJson(json, type);

        if(classes == null){
            classes = new ArrayList<>();
        }
    }

    private static class Classes{
        private String classes;


        Classes(String classes) {
            this.classes = classes;

        }
        Classes(){}

        @NonNull
        @Override
        public String toString() {
            return "To-Do: " + classes;
        }

    }

}