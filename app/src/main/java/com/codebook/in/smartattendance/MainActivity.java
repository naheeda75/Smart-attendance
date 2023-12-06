package com.codebook.in.smartattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText faculty_id,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_main);

        faculty_id=findViewById(R.id.faculty_id);
        password=findViewById(R.id.password1);
    }

    public void faculty_login(View view) {
        String uname=faculty_id.getText().toString();
        String pwd=password.getText().toString();
        if (uname.trim().length()>0 && pwd.trim().length()>0) {
            if (uname.equalsIgnoreCase("CVRCSITF011") && pwd.equalsIgnoreCase("CVRCSITF011")) {
//                if (uname.equalsIgnoreCase("CVRCSITF011") && pwd.equalsIgnoreCase("CVRCSITF011")) {
                startActivity(new Intent(MainActivity.this, AdminHome.class));
            } else if(uname.equalsIgnoreCase("CVRCSITF010") && pwd.equalsIgnoreCase("CVRCSITF010") ||
                    uname.equalsIgnoreCase("CVRCSITF011") && pwd.equalsIgnoreCase("CVRCSITF011") ||
                    uname.equalsIgnoreCase("CVRCSEF012") && pwd.equalsIgnoreCase("CVRCSEF012") ||
                    uname.equalsIgnoreCase("CVRCSEF013") && pwd.equalsIgnoreCase("CVRCSEF013") ||
                    uname.equalsIgnoreCase("CVRMECHF014") && pwd.equalsIgnoreCase("CVRMECHF014") ||
                    uname.equalsIgnoreCase("CVRMECHF015") && pwd.equalsIgnoreCase("CVRMECHF015") ||
                    uname.equalsIgnoreCase("CVRCVLF016") && pwd.equalsIgnoreCase("CVRCVLF016") ||
                    uname.equalsIgnoreCase("CVRCVLF017") && pwd.equalsIgnoreCase("CVRCVLF017") ||
                    uname.equalsIgnoreCase("CVRECEF018") && pwd.equalsIgnoreCase("CVRECEF018") ||
                    uname.equalsIgnoreCase("CVRECEF019") && pwd.equalsIgnoreCase("CVRECEF019") ||
                    uname.equalsIgnoreCase("CVREIEF020") && pwd.equalsIgnoreCase("CVREIEF020") ||
                    uname.equalsIgnoreCase("CVREIEF021") && pwd.equalsIgnoreCase("CVREIEF021") ||
                    uname.equalsIgnoreCase("CVREEEF022") && pwd.equalsIgnoreCase("CVREEEF022") ||
                    uname.equalsIgnoreCase("CVREEEF023") && pwd.equalsIgnoreCase("CVREEEF023")) {
                startActivity(new Intent(MainActivity.this, FacultyHome.class));
            }else
                alert();
            }
        else
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Login status");
            alert.setCancelable(true);
            alert.setMessage("Please provide your login details....");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alert.show();
        }
    }
    public void alert(){
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("Faculty Login")
                .setMessage("Invalid Login...Please try again")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).setCancelable(false);
        alert.show();
    }
}