package com.codebook.in.smartattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import kotlin.text._OneToManyTitlecaseMappingsKt;

public class FacultyStatisticsResult extends AppCompatActivity {

//    private Button mPickDateButton;
    ListView ListView1;
    SQLiteDatabase db;
    public String class_year, class_branch, class_section, rollno, name;
    TextView student_rollno, student_name;

    RelativeLayout relativeLayout;
    Spinner subjectSpinner;
    PieChart pieChart;
    byte[] image;
    ArrayList<String> ar = new ArrayList<>();
    TextView tv1, tv2, tv3, tv4;
    String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_faculty_statistics_result);

        class_year = getIntent().getStringExtra("year");
        class_branch = getIntent().getStringExtra("branch");
        class_section = getIntent().getStringExtra("section");
        rollno = getIntent().getStringExtra("rollno");
        name = getIntent().getStringExtra("name");

        student_rollno = findViewById(R.id.student_1);
        student_name = findViewById(R.id.student_2);
//        mPickDateButton = findViewById(R.id.pick_date_button);
        relativeLayout = findViewById(R.id.relativeLayout);
        subjectSpinner = findViewById(R.id.sp1);
        pieChart = findViewById(R.id.piechart);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);

        relativeLayout.setVisibility(View.INVISIBLE);

        Toast.makeText(this, class_year + " " + class_branch + " " + class_section + " " + rollno + " " + name, Toast.LENGTH_SHORT).show();

//        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
//        materialDateBuilder.setTitleText("SELECT A DATE");
//
//        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();
//
//        MaterialDatePicker<Long> datePicker = materialDateBuilder.build();
//
//        // handle select date button which opens the
//        // material design date picker
//        mPickDateButton.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // getSupportFragmentManager() to
//                        // interact with the fragments
//                        // associated with the material design
//                        // date picker tag is to get any error
//                        // in logcat
//                        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
//                    }
//                });
//
//        materialDatePicker.addOnPositiveButtonClickListener(
//                new MaterialPickerOnPositiveButtonClickListener<Long>() {
//                    @SuppressLint("SetTextI18n")
//                    @Override
//                    public void onPositiveButtonClick(Long selection) {
//
//                        // if the user clicks on the positive
//                        // button that is ok button update the
//                        // selected date
//                        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//                        calendar.setTimeInMillis(selection);
//                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
//                        formattedDate = format.format(calendar.getTime());
//                        mPickDateButton.setText(formattedDate);
//                        Toast.makeText(FacultyStatisticsResult.this, "Selected Date: " + formattedDate, Toast.LENGTH_SHORT).show();
//                        // in the above statement, getHeaderText
//                        // is the selected date preview from the
//                        // dialog
////                        relativeLayout.setVisibility(View.VISIBLE);
//                    }
//                });

        student_rollno.setText(rollno);
        student_name.setText(name);

        getSubjects();
    }

    public void getSubjects(){
        ar.add("Select Subject");
        db=openOrCreateDatabase("smart_attendance",MODE_PRIVATE,null);
        try {
            Cursor c = db.rawQuery("select distinct subject from attendance_details", null);
            if (c.moveToNext() && c.getCount()>0) {
                do {
                    String n = c.getString(0);
                    ar.add(n);
                    subjectSpinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, ar));
                }
                while (c.moveToNext());

            }
            else{
                Toast.makeText(this, "No data found for this record!", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(),ProductDetails.class);
//                intent.putExtra("cust_email",Email);
//                intent.putExtra("prod_name",ProdName);
//                intent.putExtra("img",image);
//                startActivity(intent);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        subjectSpinner.setSelection(0);

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    relativeLayout.setVisibility(View.VISIBLE);
                    setData();}
                else{
                    relativeLayout.setVisibility(View.INVISIBLE);
                    Toast.makeText(FacultyStatisticsResult.this, "Please select subject", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setData() {
        String _tot = "";
        String _pres = "";
        String _abs = "";
        db = openOrCreateDatabase("smart_attendance", MODE_PRIVATE, null);

        try {

//            tv1.setText("");
//            tv2.setText("");
//            tv3.setText("");

            Cursor total = db.rawQuery("SELECT DISTINCT COUNT(subject) as Total from attendance_details where year='" + class_year + "' and branch='" + class_branch + "' and section='" + class_section + "' and subject='"+subjectSpinner.getSelectedItem().toString()+"';", null);
            Cursor pos = db.rawQuery("SELECT DISTINCT COUNT(subject) FROM attendance_details WHERE year='" + class_year + "' and branch='" + class_branch + "' and section='" + class_section + "' and subject='"+subjectSpinner.getSelectedItem().toString()+"' and presentees LIKE '%" + rollno + "%';", null);
            Cursor neg = db.rawQuery("SELECT DISTINCT COUNT(subject) FROM attendance_details WHERE year='" + class_year + "' and branch='" + class_branch + "' and section='" + class_section + "' and subject='"+subjectSpinner.getSelectedItem().toString()+"' and absentees LIKE '%" + rollno + "%';", null);

            if (total.moveToNext() && total.getCount() > 0) {
                do {
                    _tot = total.getString(0);
                } while (total.moveToNext());
            }
            tv1.setText(_tot);

            if (pos.moveToNext() && pos.getCount() > 0) {
                do {
                    _pres = pos.getString(0);
                } while (pos.moveToNext());
            }
            tv2.setText(_pres);
//            tv3.setText(Integer.parseInt(_tot) - Integer.parseInt(_pres));
            if (neg.moveToNext() && neg.getCount() > 0) {
                do {
                    _abs = neg.getString(0);
                } while (neg.moveToNext());
            }
            tv3.setText(_abs);

            float val = Float.valueOf(_pres)/Float.valueOf(_tot);
            float percentage = val * 100;
            String s = String.format("%.2f",percentage);
            tv4.setText(s + "%");

            relativeLayout.setVisibility(View.VISIBLE);

            if(tv1.getText().toString().equals(""))
            {
                relativeLayout.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"No Record found...",Toast.LENGTH_LONG).show();
            }

//            Cursor neg = db.rawQuery("SELECT 100 * (SELECT COUNT(date) FROM attendance_details WHERE year='" + class_year + "' and branch='" + class_branch + "' and section='" + class_section + "' and date='" + formattedDate + "' and absentees LIKE '%" + rollno + "%')/COUNT(date) as Perc from attendance_details where year='" + class_year + "' and branch='" + class_branch + "' and section='" + class_section + "' and date='" + formattedDate + "';", null);
//            if (neg.moveToNext() && neg.getCount() > 0) {
//                do {
//                    _neg = neg.getString(0);
//                } while (neg.moveToNext());
//            }

            pieChart.clearChart();
            pieChart.addPieSlice(new PieModel("Present",
                    Integer.parseInt(tv2.getText().toString()),
                    Color.parseColor("#4CAF50")));

            pieChart.addPieSlice(
                    new PieModel(
                            "Absent",
                            Integer.parseInt(tv3.getText().toString()),
                            Color.parseColor("#F44336")));

            pieChart.startAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}