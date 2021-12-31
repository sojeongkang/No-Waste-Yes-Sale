package com.example.myhackaton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FoodregisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodregister);
    }
    public void bt_food_upload(View v){
        Intent intent = new Intent(getApplicationContext(), Host_mainActivity.class);
        startActivity(intent);
    }

}
