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

public class AdminClassesActivity extends AppCompatActivity {

    ListView ListView1;
    ArrayList<Classes> list;
    ClassesAdapter adapter = null;
    SQLiteDatabase db;

    DBHelper controller = new DBHelper(this);
    private static final int PERMISSION_REQUEST_MEMORY_ACCESS = 0;
    private static String fileType = "";
    private View mLayout;
    private static String extensionXLS = "XLS";
    private static String extensionXLXS = "XLXS";
    ActivityResultLauncher<Intent> filePicker;
    ListView lv;
    String class_year, class_branch, class_section, class_subject;
    ArrayList<ExcelSheetData> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_admin_classes);

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
                Intent intent = new Intent(AdminClassesActivity.this, AdminClassDetailsActivity.class);
                intent.putExtra("year",class_year.getText().toString());
                intent.putExtra("branch",class_branch.getText().toString());
                intent.putExtra("section",class_section.getText().toString());
                startActivity(intent);
            }
        });

        filePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent intent1 = result.getData();

                        Uri uri = intent1.getData();
                        ReadExcelFile(AdminClassesActivity.this
                                , uri);
                    }
                });
        FillList();
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


    private boolean CheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestStoragePermission();
            return false;
        }
    }

    public void FillList() {
        try {
            if (controller == null) {
                DBHelper controller = new DBHelper(AdminClassesActivity.this);
            }
            ArrayList<HashMap<String, String>> myList = controller.getProducts();
            if (myList.size() != 0) {
                lv = findViewById(R.id.ListView4);
                ListAdapter adapter = new SimpleAdapter(AdminClassesActivity.this, myList,
                        R.layout.list_row, new String[]{DBHelper.Year, DBHelper.Branch, DBHelper.Section},
                        new int[]{R.id.class_year, R.id.class_branch, R.id.class_section});
                lv.setAdapter(adapter);
            }
//            db = openOrCreateDatabase("smart_attendance", MODE_PRIVATE, null);
//            try {
//                db.beginTransaction();
//                db.execSQL("delete from branch_wise_student_details");
//                db.setTransactionSuccessful();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            } finally {
//                db.endTransaction();
//            }
        } catch (Exception ex) {
            Toast("FillList error: " + ex.getMessage(), ex);
        }
    }

    public void ReadExcelFile(AdminClassesActivity context, Uri uri) {
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

          FillList();
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
        Toast.makeText(AdminClassesActivity.this, message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                OpenFilePicker();
            } else {
                Toast.makeText(this, "Storage Access Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestStoragePermission() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(AdminClassesActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        } else {
            Toast.makeText(this, "Storage Unavailable", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
    }

    public void OpenFilePicker() {
        try {
            if (CheckPermission()) {
                ChooseFile();
            }
            else
            {
                requestStoragePermission();
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

    public void goback(View view) {
        this.finish();
//        startActivity(new Intent(AdminClassesActivity.this,AdminHome.class));
    }
}