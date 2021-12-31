package com.example.myhackaton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.TextView;

public class PlusActivity extends AppCompatActivity {

    TextView Tv_category, Tv_foodkind, Tv_condition ;
    ScrollView mainLayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus);

        Tv_category=(TextView)findViewById(R.id.tv_category);
        Tv_foodkind=(TextView)findViewById(R.id.tv_foodkind);
        Tv_condition=(TextView)findViewById(R.id.tv_condition);
        mainLayer = (ScrollView) findViewById(R.id.ScrollView1);

        Button bt_category = (Button) findViewById(R.id.bt_category);
        bt_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlusActivity.this, CategoryCheckActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        Intent it=getIntent();      //인텐트 받기 선언

        //이전에 보냈던 it_check의 값을 받아 str에 저장
        String str= it.getStringExtra("it_check");    //즉 str = "option1" 또는 "option2" 가 들어있음
        String str2= it.getStringExtra("it_check2");
        String str3= it.getStringExtra("it_check3");
        Tv_category.setText(str);
        Tv_foodkind.setText(str2);
        Tv_condition.setText(str3);

    }

    public void bt_region(View v){
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        startActivity(intent);
    }
    public void bt_friend(View v) {
        Intent intent = new Intent(getApplicationContext(), FriendActivity.class);
        startActivity(intent);
    }
    public void bt_save_plus(View v) {
        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
        startActivity(intent);
    }

}
