package com.example.calendarapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.add_exam);
        fab.setOnClickListener(view -> openExamAdder());
    }

    private void openExamAdder() {
        Intent intent = new Intent(this, ExamAdder.class);
        startActivity(intent);
    }
}