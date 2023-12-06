package com.codebook.in.smartattendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class AttendanceHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "smart_attendance";

    private static final String ATTENDANCE_TABLE_NAME = "attendance";

    private static final String ATTENDANCE_TABLE_CREATE =
            "CREATE TABLE " + ATTENDANCE_TABLE_NAME + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "date TEXT, " +
                    "roll_no TEXT, " +
                    "name TEXT, " +
                    "subject TEXT, " +
                    "status TEXT)";

    public AttendanceHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ATTENDANCE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ATTENDANCE_TABLE_NAME);
        onCreate(db);
    }


    public long insertAttendanceRecord(String date, String rollNo, String name,String subject, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("roll_no", rollNo);
        values.put("name", name);
        values.put("subject",subject);
        values.put("status", status);
        long result = db.insert(ATTENDANCE_TABLE_NAME, null, values);
        db.close();
        return result;
    }

    public Cursor getAttendanceRecordsForDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT roll_no, name, status FROM " + ATTENDANCE_TABLE_NAME +
                " WHERE date = ?", new String[] {date});
        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }
}
