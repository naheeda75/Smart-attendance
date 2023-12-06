package com.codebook.in.smartattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FacultyStatistics extends AppCompatActivity {

    ListView ListView1;
    ArrayList<Classes> list;
    ClassesAdapter adapter = null;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_faculty_statistics);

        ListView1 = findViewById(R.id.ListView4);
        list = new ArrayList<>();
        adapter = new ClassesAdapter(this, R.layout.list_row, list);
        ListView1.setAdapter(adapter);

        db = openOrCreateDatabase("smart_attendance", MODE_PRIVATE, null);
        try {
            db.beginTransaction();
            db.execSQL("create table if not exists branch_wise_student_details(student_id INTEGER PRIMARY KEY AUTOINCREMENT,student_rollno varchar,student_name varchar,student_year varchar,student_branch varchar,student_section varchar)");
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        loadData();

        ListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView class_year = (TextView) view.findViewById(R.id.class_year);
                TextView class_branch = (TextView) view.findViewById(R.id.class_branch);
                TextView class_section = (TextView) view.findViewById(R.id.class_section);
                Intent intent = new Intent(FacultyStatistics.this, FacultyStatisticsDetails.class);
                intent.putExtra("year",class_year.getText().toString());
                intent.putExtra("branch",class_branch.getText().toString());
                intent.putExtra("section",class_section.getText().toString());
                startActivity(intent);
            }
        });

    }

    public void loadData(){
        Cursor cursor = db.rawQuery("select distinct student_year,student_branch,student_section from branch_wise_student_details;",null);
        if(cursor!=null && cursor.getCount()>0) {
            list.clear();
            while (cursor.moveToNext()) {
                String year = cursor.getString(0);
                String branch = cursor.getString(1);
                String section = cursor.getString(2);

                list.add(new Classes(year, branch, section));
            }
            adapter.notifyDataSetChanged();
        }
    }



    public void goback(View view) {
        this.finish();
//        startActivity(new Intent(AdminClassesActivity.this,AdminHome.class));
    }

}