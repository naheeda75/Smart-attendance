package com.codebook.in.smartattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_admin_home);
    }

    public void classes(View view) {
        Intent intent = new Intent(getApplicationContext(), AdminClassesActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        startActivity(new Intent(AdminHome.this,MainActivity.class));
        finish();
    }
}