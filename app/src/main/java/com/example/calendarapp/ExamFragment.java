package com.example.calendarapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExamFragment extends Fragment {

    private ArrayList<Classes> classes;
    private ArrayAdapter<Classes> adapter;

    private EditText eiditCourseTime, eiditCourseInstructor;
    private Button btnClassAdd, btnClassEdit, btnSelectDate;
    private ListView listClass;
    private Calendar selectedDate = Calendar.getInstance();

    public ExamFragment() {
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
        View view = inflater.inflate(R.layout.fragment_exam, container, false);
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, classes);

        eiditCourseTime = view.findViewById(R.id.editTextTime);
        eiditCourseInstructor = view.findViewById(R.id.editTextLocation);
        btnClassAdd = view.findViewById(R.id.addButton);
        btnClassEdit = view.findViewById(R.id.editExammBotton);
        listClass = view.findViewById(R.id.listViewExams);
        listClass.setAdapter(adapter);

        // Button for selecting exam date
        btnSelectDate = view.findViewById(R.id.btnSelectDate);
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

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
                updateClass();
            }
        });

        //getting index when click on listview
        listClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString() + " has been selected";
                int indexVal = position;
                Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
            }
        });

        //Delete listview class when double click
        listClass.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString() + " has been deleted";
                Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
                int indexVal = position;
                delete();
                adapter.notifyDataSetChanged();
                indexVal = 0; //reset
                return true;
            }
        });

        return view;
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate.set(year, month, dayOfMonth);
                        btnSelectDate.setText(getFormattedDate(selectedDate.getTime()));
                    }
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private static String getFormattedDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    private void addClass() {
        String time = eiditCourseTime.getText().toString();
        String instructor = eiditCourseInstructor.getText().toString();

        if (!time.isEmpty() && !instructor.isEmpty() && selectedDate != null) {
            Classes newClass = new Classes(time, instructor, selectedDate.getTime());
            this.classes.add(newClass);
            adapter.notifyDataSetChanged();

            // Clear input fields
            eiditCourseTime.getText().clear();
            eiditCourseInstructor.getText().clear();
            btnSelectDate.setText("Select Exam Date");
        }
        saveDate();
    }

    private void updateClass() {
        String time = eiditCourseTime.getText().toString();
        String instructor = eiditCourseInstructor.getText().toString();

        if (!time.isEmpty() && !instructor.isEmpty() && selectedDate != null) {
            Classes newClass = new Classes(time, instructor, selectedDate.getTime());
            this.classes.set(0, newClass);
            adapter.notifyDataSetChanged();

            eiditCourseTime.getText().clear();
            eiditCourseInstructor.getText().clear();
            btnSelectDate.setText("Select Exam Date");
        }
        saveDate();
    }

    private void delete() {
        this.classes.remove(0);
        saveDate();
    }

    private void saveDate() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("classShare2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(classes);
        editor.putString("list2", json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("classShare2", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list2", null);
        Type type = new TypeToken<ArrayList<Classes>>() {
        }.getType();
        classes = gson.fromJson(json, type);

        if (classes == null) {
            classes = new ArrayList<>();
        }
    }

    private static class Classes {
        private String time;
        private String instructor;
        private Date examDate;

        Classes(String time, String instructor, Date examDate) {
            this.time = time;
            this.instructor = instructor;
            this.examDate = examDate;
        }

        @NonNull
        @Override
        public String toString() {
            return "Exam Date: " + getFormattedDate(examDate) + ", Time: " + time + ", Location: " + instructor;
        }
    }
}