package com.example.myhackaton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myhackaton.R;
import com.example.myhackaton.RootActivity;
import com.example.myhackaton.auth.FirebaseAuth;

public class Guest_mainActivity extends RootActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_guest_main);
        super.onCreate(savedInstanceState);
    }
    public void bt_foodcalendar(View v){
        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
        startActivityWithAni(intent);
    }
    public void bt_alarm(View v){
        Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
        startActivityWithAni(intent);
    }
    public void bt_logout(View v){
        FirebaseAuth.logout();
        mPreferenceHelper.removeCurrentAccount();
        startActivityWithAni(new Intent(this, MainActivity.class));
        onBackPressed();
    }
}
