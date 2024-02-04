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

    private ArrayList<Exam> exams;
    private ArrayAdapter<Exam> adapter;

    private EditText examDate, examTime;
    private Button btnAdd, btnEdit, btnSelectDate;
    private ListView listExam;
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
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, exams);

        examDate = view.findViewById(R.id.editTextTime);
        examTime = view.findViewById(R.id.editTextLocation);
        btnAdd = view.findViewById(R.id.addButton);
        btnEdit = view.findViewById(R.id.editExammBotton);
        listExam = view.findViewById(R.id.listViewExams);
        listExam.setAdapter(adapter);

        // Button for selecting exam date
        btnSelectDate = view.findViewById(R.id.btnSelectDate);
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        //add data
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExam();
            }
        });

        //Update data
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateExam();
            }
        });

        //getting index when click on listview
        listExam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString() + " has been selected";
                int indexVal = position;
                Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
            }
        });

        //Delete listview class when double click
        listExam.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

    private void addExam() {
        String time = examDate.getText().toString();
        String instructor = examTime.getText().toString();

        if (!time.isEmpty() && !instructor.isEmpty() && selectedDate != null) {
            Exam newClass = new Exam(time, instructor, selectedDate.getTime());
            this.exams.add(newClass);
            adapter.notifyDataSetChanged();

            // Clear input fields
            examDate.getText().clear();
            examTime.getText().clear();
            btnSelectDate.setText("Select Exam Date");
        }
        saveDate();
    }

    private void updateExam() {
        String time = examDate.getText().toString();
        String instructor = examTime.getText().toString();

        if (!time.isEmpty() && !instructor.isEmpty() && selectedDate != null) {
            Exam newClass = new Exam(time, instructor, selectedDate.getTime());
            this.exams.set(0, newClass);
            adapter.notifyDataSetChanged();

            examDate.getText().clear();
            examTime.getText().clear();
            btnSelectDate.setText("Select Exam Date");
        }
        saveDate();
    }

    private void delete() {
        this.exams.remove(0);
        saveDate();
    }

    private void saveDate() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("classShare2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(exams);
        editor.putString("list2", json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("classShare2", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list2", null);
        Type type = new TypeToken<ArrayList<Exam>>() {
        }.getType();
        exams = gson.fromJson(json, type);

        if (exams == null) {
            exams = new ArrayList<>();
        }
    }

    private static class Exam {
        private String time;
        private String instructor;
        private Date examDate;

        Exam(String time, String instructor, Date examDate) {
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