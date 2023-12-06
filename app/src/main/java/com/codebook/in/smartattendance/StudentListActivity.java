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
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Iterator;

public class StudentListActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_student_list);

        class_year = getIntent().getStringExtra("year");
        class_branch = getIntent().getStringExtra("branch");
        class_section = getIntent().getStringExtra("section");
        class_subject = getIntent().getStringExtra("subject");

        Toast.makeText(this, class_branch + " " + class_year + " " + class_section, Toast.LENGTH_SHORT).show();

        filePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent intent1 = result.getData();

                        Uri uri = intent1.getData();
                        ReadExcelFile(StudentListActivity.this
                                , uri);

                    }
                });
        FillList();
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
                DBHelper controller = new DBHelper(StudentListActivity.this);
            }
            ArrayList<HashMap<String, String>> myList = controller.getProducts();
            if (myList.size() != 0) {
                lv = findViewById(R.id.ListView3);
                ListAdapter adapter = new SimpleAdapter(StudentListActivity.this, myList,
                        R.layout.list_students, new String[]{DBHelper.Rollno, DBHelper.Name, DBHelper.Year, DBHelper.Branch, DBHelper.Section,},
                        new int[]{R.id.student_rollno, R.id.student_name, R.id.class_year,R.id.class_branch,R.id.class_section});
                lv.setAdapter(adapter);
            }
        } catch (Exception ex) {
            Toast("FillList error: " + ex.getMessage(), ex);
        }
    }

    public void ReadExcelFile(StudentListActivity context, Uri uri) {
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
        Toast.makeText(StudentListActivity.this, message, Toast.LENGTH_LONG).show();

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

            ActivityCompat.requestPermissions(StudentListActivity.this,
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
//        try {
//            InputStream inputStream = getAssets().open("cvsr.xlsx");
//            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
//            XSSFSheet sheet = workbook.getSheetAt(0);
//            Iterator<Row> rowIterator = sheet.iterator();
//            while (rowIterator.hasNext()) {
//                Row row = rowIterator.next();
//                String rollno = row.getCell(0).getStringCellValue();
//                String name = row.getCell(1).getStringCellValue();
//                ExcelSheetData data = new ExcelSheetData(rollno, name);
//                dataList.add(data);
//                Toast.makeText(this, "Data: " + dataList, Toast.LENGTH_SHORT).show();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
