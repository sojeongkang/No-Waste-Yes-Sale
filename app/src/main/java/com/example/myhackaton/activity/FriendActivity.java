package com.example.myhackaton.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import com.example.myhackaton.L;
import com.example.myhackaton.R;
import com.example.myhackaton.RootActivity;


public class FriendActivity extends RootActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_friend);
        super.onCreate(savedInstanceState);

        Button bt_friend_add = (Button) findViewById(R.id.bt_friend_add);

        bt_friend_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView Friend1 = (TextView)findViewById(R.id.friend1);
                TextView Friend2 = (TextView)findViewById(R.id.friend2);
                TextView Friend3 = (TextView)findViewById(R.id.friend3);
                TextView Friend4 = (TextView)findViewById(R.id.friend4);

                EditText Find1 = (EditText)findViewById(R.id.find1);

                String ReturnEditText;

                ReturnEditText = Find1.getText().toString();
                Friend1.setText(ReturnEditText);
            }
        });

        Button bt_save_friend = (Button) findViewById(R.id.bt_save_friend);
        final TextView friend1 = (TextView)findViewById(R.id.friend1);

        bt_save_friend.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(FriendActivity.this, PlusActivity.class); // MainActivity.java로  보내기 위한  인텐트 선언
                intent.putExtra("friend1",friend1.getText().toString()); // it_check 라는 이름하에 체크된 값을 전달
                setResult(Activity.RESULT_OK, intent);
                onBackPressed();
            }
        });

    }
}