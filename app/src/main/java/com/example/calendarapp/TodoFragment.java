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

    private ArrayList<Todo> todo;
    private ArrayAdapter<Todo> adapter;

    private EditText todoText;
    private Button todoAdd, todoEdit;
    private ListView todoListView;
    private String item;
    private int indexVal;
    private Todo todoVal;

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
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, todo);
        todoText = view.findViewById(R.id.todoText);
        todoAdd = view.findViewById(R.id.todoAdd);
        todoEdit = view.findViewById(R.id.todoEdit);
        todoListView = view.findViewById(R.id.todoListView);
        todoListView.setAdapter(adapter);
        //add data
        todoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtodo();
            }
        });

        //Update data
        todoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        //getting index when click on listview
        todoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString()+"has been selected";
                indexVal = position;
                Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
            }
        });

        //Delete listview class when double click
        todoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

    private void addtodo() {
        String classes = todoText.getText().toString();

        if (!classes.isEmpty()) {
            Todo newClass = new Todo(classes);
            this.todo.add(newClass);
            adapter.notifyDataSetChanged();

            // Clear input fields
            todoText.getText().clear();
        }
        saveDate();
    }

    //Update class
    private void update(){
        String classes = todoText.getText().toString();
        Todo newClass = new Todo(classes);

        this.todo.set(indexVal,newClass);
        adapter.notifyDataSetChanged();

        todoText.getText().clear();
        indexVal = 0; //reset index
        saveDate();

    }

    //Delete
    private void delete(){
        this.todo.remove(indexVal);
        saveDate();
    }
    //save data
    private void saveDate(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("classShare3", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(todo);
        editor.putString("list3",json);
        editor.apply();
    }
    private void loadData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("classShare3", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list3",null);
        Type type = new TypeToken<ArrayList<Todo>>() {}.getType();
        todo = gson.fromJson(json, type);

        if(todo == null){
            todo = new ArrayList<>();
        }
    }

    private static class Todo {
        private String todo;


        Todo(String todo) {
            this.todo = todo;

        }
        Todo(){
            todo ="Long click to remove todo list";
        }

        @NonNull
        @Override
        public String toString() {
            return "To-Do: " + todo;
        }

    }

}