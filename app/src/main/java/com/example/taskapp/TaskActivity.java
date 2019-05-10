package com.example.taskapp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.taskapp.ui.main.Model.Task;

import java.util.Calendar;
import java.util.Date;

public class TaskActivity extends AppCompatActivity {

    private TextInputLayout textInputLayoutTitle;
    private TextInputLayout textInputLayoutDescription;
    private TextInputEditText textInputEditTextTitle;
    private TextInputEditText textInputEditTextDescription;

    long time;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        textInputLayoutTitle = findViewById(R.id.title_text_input);
        textInputEditTextTitle = findViewById(R.id.title_input_edit_text);

        textInputLayoutDescription = findViewById(R.id.description_text_input);
        textInputEditTextDescription = findViewById(R.id.description_input_edit_text);

        task = (Task) getIntent().getSerializableExtra("task");
        if (task !=null){
            textInputEditTextTitle.setText(task.getTitle());
            textInputEditTextDescription.setText(task.getDescription());
        }


        textInputEditTextTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textInputLayoutTitle.setVisibility(View.VISIBLE);
                        }
                    },100);
                }else {
                    if (textInputEditTextTitle.getText().length()>0){
                        textInputLayoutTitle.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        textInputLayoutDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textInputLayoutDescription.setVisibility(View.VISIBLE);
                        }
                    },100);
                }else {
                    if (textInputEditTextDescription.getText().length()>0){
                        textInputLayoutDescription.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void onClickDate(View view) {
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                0,
                null,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                "ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int day = datePickerDialog.getDatePicker().getDayOfMonth();
                int month = datePickerDialog.getDatePicker().getMonth();
                int year = datePickerDialog.getDatePicker().getYear();

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(year,month,day);
                time = calendar1.getTimeInMillis();

            }
        });
        datePickerDialog.show();
    }

    public void onClickSave(View view) {
        String title = textInputEditTextTitle.getText().toString().trim();
        String description = textInputEditTextDescription.getText().toString().trim();
        if (time ==0) time = System.currentTimeMillis();

        if (task !=null){
            task.setTitle(title);
            task.setDescription(description);
            task.setTime(time);
            App.getInstance().getAppDatabase().taskDao().update(task);
        }else {

            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setTime(time);
            App.getInstance().getAppDatabase().taskDao().insert(task);
        }
        finish();
    }
}
