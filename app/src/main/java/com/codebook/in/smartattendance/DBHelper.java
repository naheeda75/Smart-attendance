package com.codebook.in.smartattendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper {

    String TAG = "DBAdapter";

    public static final String Tablename = "branch_wise_student_details";
    public static final String id = "_id";// 0 integer
    public static final String Rollno = "Student_Rollno";// 1 text(String)
    public static final String Name = "Student_Name";// 2 integer
    public static final String Year = "Student_Year";
    public static final String Branch = "Student_Branch";
    public static final String Section = "Student_Section";
    public static final String Subject = "Student_Subject";

    private SQLiteDatabase db;
    private Database dbHelper;

    private static final int VERSION = 1;
    private static final String DB_NAME = "smart_attendance";

    public DBHelper(Context context) {
        dbHelper = new Database(context);
    }

    public void open() {
        if (null == db || !db.isOpen()) {
            try {
                db = dbHelper.getWritableDatabase();
            } catch (SQLiteException sqLiteException) {
            }
        }
    }

    public void close() {
        if (db != null) {
            db.close();
        }
    }

    public int insert(String table, ContentValues values) {
        try {
            db = dbHelper.getWritableDatabase();
            int y = (int) db.insert(table, null, values);
            db.close();
            Log.e("Data Inserted", "Data Inserted");
            Log.e("y", y + "");
            return y;
        } catch (Exception ex) {
            Log.e("Error Insert", ex.getMessage().toString());
            return 0;
        }
    }

    public void delete() {
        db.execSQL("delete from " + Tablename);
    }

    public Cursor getAllRow(String table) {
        return db.query(table, null, null, null, null, null, id);
    }

    public ArrayList<HashMap<String, String>> getProducts() {
        ArrayList<HashMap<String, String>> prolist;
        prolist = new ArrayList<HashMap<String, String>>();
        String selectQuery = "select distinct student_year,student_branch,student_section from " + Tablename;
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
//                map.put(Rollno, cursor.getString(1));
//                map.put(Name, cursor.getString(2));
                map.put(Year, cursor.getString(0));
                map.put(Branch, cursor.getString(1));
                map.put(Section, cursor.getString(2));

                prolist.add(map);
            } while (cursor.moveToNext());
        }
        return prolist;
    }

    private class Database extends SQLiteOpenHelper {
        private static final int VERSION = 1;
        private static final String DB_NAME = "smart_attendance";

        public Database(Context context) {
            super(context, DB_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String create_sql = "CREATE TABLE IF NOT EXISTS " + Tablename + "("
                    + id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Rollno + " TEXT ," + Name + " TEXT ,"
                    + Year + " TEXT ," + Branch + " TEXT , " + Section + ")";
            db.execSQL(create_sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Tablename);
        }

    }
}
