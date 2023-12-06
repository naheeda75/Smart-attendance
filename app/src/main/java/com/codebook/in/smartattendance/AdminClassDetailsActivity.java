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
import android.widget.Toast;

import java.util.ArrayList;

public class AdminClassDetailsActivity extends AppCompatActivity {

    ListView ListView1;
    ArrayList<Students> list;
    StudentsAdapter adapter = null;
    SQLiteDatabase db;
    String class_year,class_branch,class_section;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_admin_class_details);

        class_year = getIntent().getStringExtra("year");
        class_branch = getIntent().getStringExtra("branch");
        class_section = getIntent().getStringExtra("section");

        Toast.makeText(this, class_year + " " + class_branch + "-" + class_section, Toast.LENGTH_SHORT).show();

        ListView1 = findViewById(R.id.ListView5);
        list = new ArrayList<>();
        adapter = new StudentsAdapter(this, R.layout.list_students, list);
        ListView1.setAdapter(adapter);

        int year1 = Integer.parseInt(class_year);
        db = openOrCreateDatabase("smart_attendance",MODE_PRIVATE,null);
        Cursor cursor = db.rawQuery("select * from branch_wise_student_details where student_year = "+year1+" and student_branch='"+class_branch+"' and student_section='"+class_section+"'",null);
        if(cursor!=null && cursor.getCount()>0) {
            list.clear();
            while (cursor.moveToNext()) {
                String rollno = cursor.getString(1);
                String name = cursor.getString(2);
                String year = cursor.getString(3);
                String branch = cursor.getString(4);
                String section = cursor.getString(5);
                list.add(new Students(rollno, name,year,branch,section));
            }
            adapter.notifyDataSetChanged();
        }
        ListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView rollno =(TextView)view.findViewById(R.id.student_rollno);
                TextView name =(TextView)view.findViewById(R.id.student_name);
                Toast.makeText(getApplicationContext(), rollno.getText().toString() + " " + name.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}