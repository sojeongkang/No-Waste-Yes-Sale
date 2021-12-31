package com.example.myhackaton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Host_mainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_main);
    }
    public void bt_sale_plus(View v){
        Intent intent = new Intent(getApplicationContext(), FoodregisterActivity.class);
        startActivity(intent);
    }
    public void bt_logout(View v){
        Intent intent = new Intent(getApplicationContext(), Login_mainActivity.class);
        startActivity(intent);
    }
}