package com.codebook.in.smartattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class xAttendanceActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> adapter;
    String[]  array = {"apple","ball","cat","dog","elephant","fish","girl"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xattendance);

        listView=findViewById(R.id.listview_data);
        adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice,array);
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return  true;
//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.item_done){
            String item_selected = "Selected Items: \n";
            for (int i=0;i<listView.getCount();i++){
                if(listView.isItemChecked(i)){
                    item_selected+= listView.getItemAtPosition(i) + "\n";
                }
            } Toast.makeText(this, item_selected, Toast.LENGTH_LONG).show();

        }
        return  super.onOptionsItemSelected(item);
    }
}