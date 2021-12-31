package com.example.myhackaton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Host_joinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_join);
    }
    public void bt_host_join(View v){
        Intent intent = new Intent(getApplicationContext(), Login_mainActivity.class);
        startActivity(intent);
    }
}
