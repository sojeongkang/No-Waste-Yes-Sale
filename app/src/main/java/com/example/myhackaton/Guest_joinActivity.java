package com.example.myhackaton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Guest_joinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_join);
    }
    public void bt_guest_join(View v){
        Intent intent = new Intent(getApplicationContext(), Login_mainActivity.class);
        startActivity(intent);
    }

}
