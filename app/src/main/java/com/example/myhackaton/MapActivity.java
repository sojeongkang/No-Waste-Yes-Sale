package com.example.myhackaton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MapActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
    }

    public void bt_hong(View v){
        Intent intent = new Intent(getApplicationContext(), PlusActivity.class);
        startActivity(intent);
    }
    public void bt_sin(View v){
        Intent intent = new Intent(getApplicationContext(), PlusActivity.class);
        startActivity(intent);
    }
    public void bt_young(View v){
        Intent intent = new Intent(getApplicationContext(), PlusActivity.class);
        startActivity(intent);
    }
    public void bt_gun(View v){
        Intent intent = new Intent(getApplicationContext(), PlusActivity.class);
        startActivity(intent);
    }
    public void bt_mung(View v){
        Intent intent = new Intent(getApplicationContext(), PlusActivity.class);
        startActivity(intent);
    }
    public void bt_lee(View v){
        Intent intent = new Intent(getApplicationContext(), PlusActivity.class);
        startActivity(intent);
    }
    public void bt_hap(View v){
        Intent intent = new Intent(getApplicationContext(), PlusActivity.class);
        startActivity(intent);
    }
}
