package com.codebook.in.smartattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    WebView webView;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_splash);

        imageView = findViewById(R.id.logo);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View,String>(imageView,"main_anim");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this,pairs);
                    startActivity(intent,options.toBundle());
                    finish();
                }
            }
        }, 2000);


        WebView wv = (WebView) findViewById(R.id.webview);
        //wv.loadUrl("file:///android_res/drawable/load.gif");
        //wv.loadDataWithBaseURL("file:///android_res/drawable/", "<img src='load.gif' />", "text/html", "utf-8", null);

        wv.loadDataWithBaseURL("file:///android_res/drawable/", "<html><head><style type='text/css'>body{margin:auto auto;text-align:center;} img{width:100%25;} </style></head><body><img src='load.gif'/></body></html>", "text/html", "UTF-8", null);

    }
}