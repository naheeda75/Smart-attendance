package com.codebook.in.smartattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class AddClassActivity extends AppCompatActivity {

    String Faculty_Id;
    EditText Class_Year, Class_Branch, Class_Section, Class_Subject;
    TextInputLayout YearBox,BranchBox,SectionBox,SubjectBox;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_add_class);

        Faculty_Id = getIntent().getStringExtra("faculty_id");

        Class_Year = findViewById(R.id.class_year);
        Class_Branch = findViewById(R.id.class_branch);
        Class_Section = findViewById(R.id.class_section);
        Class_Subject = findViewById(R.id.class_subject);

        YearBox = findViewById(R.id.classyear);
        BranchBox=findViewById(R.id.classbranch);
        SectionBox=findViewById(R.id.classsection);
        SubjectBox=findViewById(R.id.classsubject);
    }

    public void add_class(View view) {
        String class_year = Class_Year.getText().toString();
        String class_branch = Class_Branch.getText().toString();
        String class_section = Class_Section.getText().toString();
        String class_subject = Class_Subject.getText().toString();

        if (validateBranch() && validateYear() && validateSection() && validateSubject()) {
            db = openOrCreateDatabase("smart_attendance", MODE_PRIVATE, null);
            try {

                db.beginTransaction();
                db.execSQL("create table if not exists classes(class_id INTEGER PRIMARY KEY AUTOINCREMENT,class_year varchar,class_branch varchar,class_section varchar,class_subject varchar)");
                db.execSQL("insert into classes(class_year,class_branch,class_section,class_subject) values('" + class_year + "','" + class_branch + "','" + class_section + "','" + class_subject + "')");
                alert();
                db.setTransactionSuccessful();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        }

    }

    private boolean validateYear() {
        String val = Class_Year.getText().toString().trim();

        if (val.isEmpty()) {
            YearBox.setError("Must Enter Year");
            YearBox.requestFocus();
            return false;
        }
        else {
            YearBox.setError(null);
            YearBox.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateBranch() {
        String val = Class_Branch.getText().toString().trim();

        if (val.isEmpty()) {
            BranchBox.setError("Must Enter Branch");
            BranchBox.requestFocus();
            return false;
        } else {
            BranchBox.setError(null);
            BranchBox.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateSection() {
        String val = Class_Section.getText().toString().trim();

        if (val.isEmpty()) {
            SectionBox.setError("Must Enter Section");
            SectionBox.requestFocus();
            return false;
        }
        else {
            SectionBox.setError(null);
            SectionBox.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateSubject() {
        String val = Class_Subject.getText().toString().trim();
        if (val.isEmpty()) {
            SubjectBox.setError("Must Enter Subject");
            SubjectBox.requestFocus();
            return false;
        }
        else {
            SubjectBox.setError(null);
            SubjectBox.setErrorEnabled(false);
            return true;
        }
    }

    public void alert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Class status");
        alert.setCancelable(true);
        alert.setMessage("Class Added Successfully");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext(), ClassesActivity.class);
                startActivity(intent);
            }
        });
        alert.show();
    }
}