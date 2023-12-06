package com.codebook.in.smartattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FacultyClassDetailsActivity extends AppCompatActivity {

    private ListView mListView;
    private AttendanceAdapter mCustomAdapter;
    private ArrayList<Attendance> mAttendanceList;
    private SQLiteDatabase mDatabase;
    private String class_year, class_branch, class_section;
    private List<String> absenteesList = new ArrayList<>();
    private List<String> presenteesList = new ArrayList<>();
    private AttendanceHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_faculty_class_details);

        dbHelper = new AttendanceHelper(this);

        class_year = getIntent().getStringExtra("year");
        class_branch = getIntent().getStringExtra("branch");
        class_section = getIntent().getStringExtra("section");

        Toast.makeText(this, class_year + " " + class_branch + "-" + class_section, Toast.LENGTH_SHORT).show();

        mAttendanceList = new ArrayList<>();
        mListView = findViewById(R.id.myListView);
        int year1 = Integer.parseInt(class_year);
        mDatabase = openOrCreateDatabase("smart_attendance", MODE_PRIVATE, null);
        // Fetch the attendance data from the "attendance" table in the database
        Cursor cursor = mDatabase.rawQuery("select * from branch_wise_student_details where student_year = " + year1 + " and student_branch='" + class_branch + "' and student_section='" + class_section + "'", null);

        // Loop through the cursor and add Attendance objects to the ArrayList
        if (cursor.moveToFirst()) {
            do {
                String rollNumber = cursor.getString(1);
                String studentName = cursor.getString(2);
                Attendance attendance = new Attendance(rollNumber, studentName, false);
                mAttendanceList.add(attendance);
            } while (cursor.moveToNext());
        }
        cursor.close();

        try {
            // Create an instance of your CustomAdapter and set it as the adapter for your ListView
            mCustomAdapter = new AttendanceAdapter(this, mAttendanceList);
            mListView.setAdapter(mCustomAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set a listener for the button that saves the attendance data
        Button saveButton = findViewById(R.id.buttonattendance);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected absentees from the ListView
                List<String> absentees = getSelectedAbsentees();
                List<String> presentees = getSelectedPresentees();
                Intent intent = new Intent(getApplicationContext(), FacultySendAttendance.class);
                intent.putExtra("year", class_year);
                intent.putExtra("branch", class_branch);
                intent.putExtra("section", class_section);
                // Pass the absentees list as an extra to the intent
                intent.putStringArrayListExtra("absentees", (ArrayList<String>) absentees);
                intent.putStringArrayListExtra("presentees", (ArrayList<String>) presentees);
                intent.putExtra("date",getCurrentDate());
                // Start the next activity
                startActivity(intent);
                // Insert attendance records for each absent student
                for (String absentee : absentees) {
                    String date = getCurrentDate();
                    String name = getNameforRollNo(absentee);
//                    dbHelper.insertAttendanceRecord(date, absentee.toString(), name,"cc" ,"absent");
                }

                for (String present : presentees) {
                    String date = getCurrentDate();
                    String name = getNameforRollNo(present);
//                    dbHelper.insertAttendanceRecord(date, absentee.toString(), name,"cc" ,"absent");
                }

                // Show a toast message to indicate successful insertion of attendance records
                Toast.makeText(FacultyClassDetailsActivity.this, "Absentees: " + absentees + " Presentees: " + presentees, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Attendance records saved.", Toast.LENGTH_SHORT).show();
            }

        });
    }
    // Method to get the selected absentees from the ListView
    private List<String> getSelectedAbsentees() {
        List<String> absentees = new ArrayList<>();
        ListView listView = findViewById(R.id.myListView);
        for (int i = 0; i < listView.getCount(); i++) {
            View view = listView.getChildAt(i);
            if (view != null) {
                CheckBox checkBox = view.findViewById(R.id.mystud_chk);
                if (checkBox.isChecked()) {
                    TextView rollno = view.findViewById(R.id.mystud_rollno);
                    String abs_rollno = rollno.getText().toString();
                    absentees.add(abs_rollno);
                }
            }
        }
        return absentees;
    }

    // Method to get the selected presentees from the ListView
    private List<String> getSelectedPresentees() {
        List<String> presentees = new ArrayList<>();
        ListView listView = findViewById(R.id.myListView);
        for (int i = 0; i < listView.getCount(); i++) {
            View view = listView.getChildAt(i);
            if (view != null) {
                CheckBox checkBox = view.findViewById(R.id.mystud_chk);
                if (!checkBox.isChecked()) {
                    TextView rollno = view.findViewById(R.id.mystud_rollno);
                    String present_rollno = rollno.getText().toString();
                    presentees.add(present_rollno);
                }
            }
        }
        return presentees;
    }
    // Method to get the roll number for a given name
    private String getNameforRollNo(String rollno) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT student_name FROM branch_wise_student_details WHERE student_rollno = ?", new String[] {rollno});
        String name = "";
        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return name;
    }
    // Method to get the current date in yyyy-MM-dd format
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    public void sendAbsenteesSms(String mobileNumber) {
        StringBuilder builder = new StringBuilder();
        builder.append("Absentees: ");
        for (String absentees : absenteesList) {
            builder.append(absentees).append(", ");
        }
        builder.delete(builder.length() - 2, builder.length() - 1); // remove last comma and space
        String message = builder.toString();

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(mobileNumber, null, message, null, null);
    }
}