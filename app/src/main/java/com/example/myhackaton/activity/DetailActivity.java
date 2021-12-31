package com.example.myhackaton.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myhackaton.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }
    public void bt_reservation(View v){
        Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
        startActivity(intent);
    }
}
