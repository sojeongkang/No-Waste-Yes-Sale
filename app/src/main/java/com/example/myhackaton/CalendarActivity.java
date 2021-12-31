package com.example.myhackaton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CalendarActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
    }

    public void bt_plus(View v){
        Intent intent = new Intent(getApplicationContext(), PlusActivity.class);
        startActivity(intent);
    }
    public void bt_before(View v){
        Intent intent = new Intent(getApplicationContext(), Guest_mainActivity.class);
        startActivity(intent);
    }
}
