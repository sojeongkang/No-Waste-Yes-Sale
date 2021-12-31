package com.example.myhackaton.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.myhackaton.R;
import com.example.myhackaton.activity.Guest_joinActivity;
import com.example.myhackaton.activity.Guest_mainActivity;
import com.example.myhackaton.activity.Host_joinActivity;
import com.example.myhackaton.activity.Host_mainActivity;


public class Login_mainActivity extends AppCompatActivity {

    RadioGroup rGroup1;
    RadioButton hostbutton, guestbutton;
    Button Newjoin, Newlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        rGroup1 = (RadioGroup)findViewById(R.id.Rgroup1);
        hostbutton = (RadioButton)findViewById(R.id.Hostbutton);
        guestbutton = (RadioButton) findViewById(R.id.Guestbutton);
        Newjoin = (Button) findViewById(R.id.bt_join);
        Newlogin = (Button) findViewById(R.id.bt_login);

        Newjoin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                switch (rGroup1.getCheckedRadioButtonId()) {
                    case R.id.Hostbutton:
                        Intent intent = new Intent(getApplicationContext(), Host_joinActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.Guestbutton:
                        Intent intent2 = new Intent(getApplicationContext(), Guest_joinActivity.class);
                        startActivity(intent2);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Account, Guest 먼저 선택하세요", Toast.LENGTH_SHORT)
                                .show();
                }
            }
        });
        Newlogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                switch (rGroup1.getCheckedRadioButtonId()) {
                    case R.id.Hostbutton:
                        Intent intent = new Intent(getApplicationContext(), Host_mainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.Guestbutton:
                        Intent intent2 = new Intent(getApplicationContext(), Guest_mainActivity.class);
                        startActivity(intent2);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Account, Guest 먼저 선택하세요", Toast.LENGTH_SHORT)
                                .show();
                }
            }
        });
    }

}
