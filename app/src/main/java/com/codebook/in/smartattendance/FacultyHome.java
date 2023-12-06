package com.codebook.in.smartattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FacultyHome extends AppCompatActivity {

    String Faculty_Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_faculty_home);

        Faculty_Id = getIntent().getStringExtra("faculty_id");
    }


//    public void classes(View view) {
//        Intent intent = new Intent(getApplicationContext(), ClassesActivity.class);
//        intent.putExtra("faculty_id", Faculty_Id);
//        startActivity(intent);
//    }

    public void attendance(View view) {
        Intent intent = new Intent(getApplicationContext(), FacultyClassesActivity.class);
        intent.putExtra("faculty_id", Faculty_Id);
        startActivity(intent);
    }

    public void statistics(View view) {
        Intent intent = new Intent(getApplicationContext(), FacultyStatistics.class);
        intent.putExtra("faculty_id", Faculty_Id);
        startActivity(intent);
    }
}