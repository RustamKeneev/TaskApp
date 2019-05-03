package com.example.taskapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.taskapp.ui.main.Adapter.SectionsPagerAdapter;
import com.example.taskapp.ui.main.Model.Task;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }

    public void onClick(View view) {
       startActivityForResult(new Intent(this,TaskActivity.class),100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode ==RESULT_OK && requestCode==100){
            Task task  = (Task) data.getSerializableExtra("task");
            Log.e("TAG","title" + task.getTitle());
            Log.e("TAG","date "+task.getDate());

        }
    }
}