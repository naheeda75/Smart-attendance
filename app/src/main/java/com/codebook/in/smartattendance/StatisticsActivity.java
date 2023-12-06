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

public class StatisticsActivity extends AppCompatActivity {

    String Faculty_Id;
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
        setContentView(R.layout.activity_statistics);
        Faculty_Id = getIntent().getStringExtra("faculty_id");

        ListView1 = findViewById(R.id.ListView3);
        list = new ArrayList<>();
        adapter = new ClassesAdapter(this, R.layout.list_row, list);
        ListView1.setAdapter(adapter);

        db = openOrCreateDatabase("smart_attendance",MODE_PRIVATE,null);
        try {

            db.beginTransaction();
            db.execSQL("create table if not exists classes(class_id INTEGER PRIMARY KEY AUTOINCREMENT,class_year varchar,class_branch varchar,class_section varchar,class_subject varchar)");
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        Cursor cursor = db.rawQuery("select * from classes",null);
        if(cursor!=null && cursor.getCount()>0) {
            list.clear();
            while (cursor.moveToNext()) {
                String year = cursor.getString(1);
                String branch = cursor.getString(2);
                String section = cursor.getString(3);
                String subject = cursor.getString(4);
                list.add(new Classes(year, branch, section));
            }
            adapter.notifyDataSetChanged();
        }

        ListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView class_year =(TextView)view.findViewById(R.id.class_year);
                Intent intent = new Intent(StatisticsActivity.this,ClassesActivity.class);
                startActivity(intent);
            }
        });
    }
    private void updateClasses(){
        // get all data from sqlite
        Cursor cursor = db.rawQuery("SELECT * FROM CLASSES",null);
        list.clear();
        while (cursor.moveToNext()) {
            String year = cursor.getString(1);
            String branch = cursor.getString(2);
            String section = cursor.getString(3);
            String subject = cursor.getString(4);
            list.add(new Classes(year, branch, section));
        }
        adapter.notifyDataSetChanged();

    }
}