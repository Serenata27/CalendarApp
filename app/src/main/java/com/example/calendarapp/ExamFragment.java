package com.example.calendarapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ExamFragment extends Fragment {

    private ArrayList<Exam> exams;
    private ArrayAdapter<Exam> adapter;

    private EditText dateEditText, timeEditText, locationEditText;
    private Button addButton;
    private ListView examListView;

    public ExamFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam, container, false);

        exams = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, exams);

        dateEditText = view.findViewById(R.id.editTextDate);
        timeEditText = view.findViewById(R.id.editTextTime);
        locationEditText = view.findViewById(R.id.editTextLocation);
        addButton = view.findViewById(R.id.addButton);
        examListView = view.findViewById(R.id.listViewExams);

        examListView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExam();
            }
        });

        return view;
    }

    private void addExam() {
        String date = dateEditText.getText().toString();
        String time = timeEditText.getText().toString();
        String location = locationEditText.getText().toString();

        if (!date.isEmpty() && !time.isEmpty() && !location.isEmpty()) {
            Exam newExam = new Exam(date, time, location);
            exams.add(newExam);
            adapter.notifyDataSetChanged();

            // Clear input fields
            dateEditText.getText().clear();
            timeEditText.getText().clear();
            locationEditText.getText().clear();
        }
    }

    private static class Exam {
        private String date;
        private String time;
        private String location;

        Exam(String date, String time, String location) {
            this.date = date;
            this.time = time;
            this.location = location;
        }

        @NonNull
        @Override
        public String toString() {
            return "Date: " + date + ", Time: " + time + ", Location: " + location;
        }
    }
}