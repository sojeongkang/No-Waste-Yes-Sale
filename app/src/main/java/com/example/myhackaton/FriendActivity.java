package com.example.myhackaton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
    }

    public void bt_friend_search(View v){
        Intent intent = new Intent(getApplicationContext(), PlusActivity.class);
        startActivity(intent);
    }
    public void bt_save_friend(View v){
        Intent intent = new Intent(getApplicationContext(), PlusActivity.class);
        startActivity(intent);
    }
}
