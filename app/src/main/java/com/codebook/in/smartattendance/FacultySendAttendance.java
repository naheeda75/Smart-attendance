package com.codebook.in.smartattendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FacultySendAttendance extends AppCompatActivity {

    private String class_year, class_branch, class_section,date;

    List<String> absentees,presentees;
    List<String> lastThreeDigitsList = new ArrayList<String>();
    TextInputEditText subject, mobile;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().setTitle("Attendance");
        }
        catch (NullPointerException e) {}
        setContentView(R.layout.activity_faculty_send_attendance);

        class_year = getIntent().getStringExtra("year");
        class_branch = getIntent().getStringExtra("branch");
        class_section = getIntent().getStringExtra("section");
        // Get the absentees list from the intent extras
        absentees = getIntent().getStringArrayListExtra("absentees");
        presentees = getIntent().getStringArrayListExtra("presentees");
        date = getIntent().getStringExtra("date");

        subject = findViewById(R.id.attendance_subject);
        mobile = findViewById(R.id.attendance_mobile);


    }
    public void send_attendance(View view) {
        AttendanceHelper helper = new AttendanceHelper(this);

        for (String rollNo : absentees) {
//            String lastThreeDigits = rollNo.substring(Math.max(0, rollNo.length() - 3));
            lastThreeDigitsList.add(rollNo);

            helper.insertAttendanceRecord(getCurrentDate(),rollNo,"",subject.getText().toString(),"Absent");
        }

//        sendSMS(mobile.getText().toString(),subject.getText().toString(),lastThreeDigitsList);
        requestSmsPermission();
    }

    private void sendSMS(String phoneNumber, String subject, List<String> absenteesList) {
        String message = "Date: " + getCurrentDate() + "\nYear: " + class_year + " " + class_branch + " " + class_section + "\nSubject: " + subject + "\nAbsentees: " + absenteesList;
//        for (String student : absenteesList) {
////            String rollNo = student.getRollNumber().substring(Math.max(0, student.getRollNumber().length() - 3));
//            message += "Absentees: " + absentees;
//        }
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);

            db = openOrCreateDatabase("smart_attendance", MODE_PRIVATE, null);
            try {

                db.beginTransaction();
                db.execSQL("create table if not exists attendance_details(year varchar,branch varchar,section varchar,subject varchar,presentees varchar,absentees varchar,date varchar)");
                db.execSQL("insert into attendance_details values('" + class_year + "','" + class_branch + "','" + class_section + "','" + subject.trim() + "','" + presentees + "','" + absentees + "','" + date + "')");
                Alert();
                db.setTransactionSuccessful();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }

            Toast.makeText(this, "SMS Sent!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private static final int PERMISSION_SEND_SMS = 123;

    private void requestSmsPermission() {

        // check permission is given
        if (ContextCompat.checkSelfPermission(FacultySendAttendance.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // request permission (see result in onRequestPermissionsResult() method)
            ActivityCompat.requestPermissions(FacultySendAttendance.this,
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSION_SEND_SMS);
        } else {
            // permission already granted run sms send
            sendSMS(mobile.getText().toString(),subject.getText().toString(),lastThreeDigitsList);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_SEND_SMS: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    sendSMS(mobile.getText().toString(), subject.getText().toString(), lastThreeDigitsList);
                } else {
                    // permission denied
                }
                return;
            }
        }
    }
    // Method to get the current date in yyyy-MM-dd format
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    public void Alert () {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Attendance status");
        alert.setCancelable(true);
        alert.setMessage("Attendance records saved and sms sent Successfully");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext(), FacultyHome.class);
                startActivity(intent);
                finish();
            }
        });
        alert.show();
    }
}