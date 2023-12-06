package com.codebook.in.smartattendance;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ClassDetailsActivity extends AppCompatActivity {

    String Faculty_Id;
    ListView ListView1;
    ArrayList<Students> list;
    StudentsAdapter adapter = null;
    SQLiteDatabase db;

    DBHelper controller = new DBHelper(this);
    private static final int PERMISSION_REQUEST_MEMORY_ACCESS = 0;
    private static String fileType = "";
    private View mLayout;
    private static String extensionXLS = "XLS";
    private static String extensionXLXS = "XLXS";
    ActivityResultLauncher<Intent> filePicker;
    ListView lv;
    String class_year,class_branch,class_section,class_subject;
    ArrayList<ExcelSheetData> dataList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_class_details);

        Faculty_Id = getIntent().getStringExtra("faculty_id");
        class_year = getIntent().getStringExtra("year");
        class_branch = getIntent().getStringExtra("branch");
        class_section = getIntent().getStringExtra("section");

        Toast.makeText(this, class_year + " " + class_branch, Toast.LENGTH_SHORT).show();

        ListView1 = findViewById(R.id.ListView4);
        list = new ArrayList<>();
        adapter = new StudentsAdapter(this, R.layout.list_students, list);
        ListView1.setAdapter(adapter);

        db = openOrCreateDatabase("smart_attendance",MODE_PRIVATE,null);
        try {
            db.beginTransaction();
            db.execSQL("create table if not exists branch_wise_student_details(student_id INTEGER PRIMARY KEY AUTOINCREMENT,student_rollno varchar,student_name varchar,student_year varchar,student_branch varchar,student_section varchar)");
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        int year1 = Integer.parseInt(class_year);
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
                TextView class_year =(TextView)view.findViewById(R.id.class_year);
                Intent intent = new Intent(ClassDetailsActivity.this,ClassDetailsActivity.class);
                startActivity(intent);
            }
        });

    filePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
    result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {

            Intent intent1 = result.getData();

            Uri uri = intent1.getData();
            ReadExcelFile(ClassDetailsActivity.this
                    , uri);

        }
    });

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


//        Cursor c = db.rawQuery("select * from branch_wise_student_details where student_year = '"+class_year+"' and student_branch='"+class_branch+"' and student_section='"+class_section+"'",null);
//        if(c!=null && c.getCount()>0) {
//            list.clear();
//            while (c.moveToNext()) {
//                String year = c.getString(1);
//                String branch = c.getString(2);
//                String section = c.getString(3);
//                list.add(new Students(year, branch, section));
//            }
//            adapter.notifyDataSetChanged();
//        }

//    FillList();
}

    private boolean CheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            Snackbar.make(mLayout, "Storage Access Required",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestStoragePermission();
                }
            }).show();

            return false;
        }
    }

    public void FillList() {
        try {
            if (controller == null) {
                DBHelper controller = new DBHelper(ClassDetailsActivity.this);
            }
            ArrayList<HashMap<String, String>> myList = controller.getProducts();
            if (myList.size() != 0) {
                lv = findViewById(R.id.ListView4);
                ListAdapter adapter = new SimpleAdapter(ClassDetailsActivity.this, myList,
                        R.layout.list_students, new String[]{DBHelper.Rollno, DBHelper.Name, DBHelper.Year, DBHelper.Branch, DBHelper.Section,},
                        new int[]{R.id.student_rollno, R.id.student_name, R.id.class_year,R.id.class_branch,R.id.class_section});
                lv.setAdapter(adapter);
            }
        } catch (Exception ex) {
            Toast("FillList error: " + ex.getMessage(), ex);
        }
    }

    public void ReadExcelFile(ClassDetailsActivity context, Uri uri) {
        try {
            InputStream inStream;
            Workbook wb = null;

            try {
                inStream = context.getContentResolver().openInputStream(uri);

                if (fileType == extensionXLS)
                    wb = new HSSFWorkbook(inStream);
                else
                    wb = new XSSFWorkbook(inStream);

                inStream.close();
            } catch (IOException e) {
//                lbl.setText("First " + e.getMessage().toString());
                e.printStackTrace();
            }

            DBHelper dbAdapter = new DBHelper(this);
            Sheet sheet1 = wb.getSheetAt(0);

            dbAdapter.open();
            dbAdapter.delete();
            dbAdapter.close();
            dbAdapter.open();
            ExcelHelper.insertExcelToSqlite(dbAdapter, sheet1);

            dbAdapter.close();

//            FillList();
        } catch (Exception ex) {
            Toast("ReadExcelFile Error:" + ex.getMessage().toString(), ex);
        }
    }

    public void ChooseFile() {
        try {
            Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            fileIntent.addCategory(Intent.CATEGORY_OPENABLE);

            if (fileType == extensionXLS)
                fileIntent.setType("application/vnd.ms-excel");
            else
                fileIntent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            filePicker.launch(fileIntent);
        } catch (Exception ex) {
            Toast("ChooseFile error: " + ex.getMessage().toString(), ex);

        }
    }

    void Toast(String message, Exception ex) {
        if (ex != null)
            Log.e("Error", ex.getMessage().toString());
        Toast.makeText(ClassDetailsActivity.this, message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_MEMORY_ACCESS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                OpenFilePicker();
            } else {
                Snackbar.make(mLayout, "Storage Access Denied",
                                Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void requestStoragePermission() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ClassDetailsActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_MEMORY_ACCESS);

        } else {
            Snackbar.make(mLayout,"Storage Unavailable", Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_MEMORY_ACCESS);
        }
    }

    public void OpenFilePicker() {
        try {
            if (CheckPermission()) {
                ChooseFile();
            }
        } catch (ActivityNotFoundException e) {
//            lbl.setText("No activity can handle picking a file. Showing alternatives.");
            e.printStackTrace();
        }

    }

    public void upload(View view) {
        OpenFilePicker();
    }
}