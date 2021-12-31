package com.example.myhackaton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;

public class CategoryCheckActivity extends AppCompatActivity {

    public String Category = "카테고리";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorycheck);

        findViewById(R.id.cb_alcohol).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_date).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_meeting).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_wedding).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_group).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_family).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_propose).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked(v); // 체크되었을 때 동작코드
            }
        });

        findViewById(R.id.cb_kfood).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked2(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_wfood).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked2(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_cfood).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked2(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_jfood).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked2(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_sfood).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked2(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_ffood).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked2(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_cafe).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked2(v); // 체크되었을 때 동작코드
            }
        });

        findViewById(R.id.cb_magic).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked2(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_gold).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked2(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_diet).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked2(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_health).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked2(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_night).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked2(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_sick).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked2(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_bulkup).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked2(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_rain).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked2(v); // 체크되었을 때 동작코드
            }
        });

        Button bt_save_category = (Button) findViewById(R.id.bt_save_category);
        bt_save_category.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                //return data
                Intent intent = new Intent(CategoryCheckActivity.this, PlusActivity.class); // MainActivity.java로  보내기 위한  인텐트 선언
                intent.putExtra("it_check", Checked(v)); // it_check 라는 이름하에 체크된 값을 전달
                intent.putExtra("it_check2", Checked2(v));
                intent.putExtra("it_check3", Checked3(v));
                startActivity(intent); // MainActivity.java를 실행하면서 값을 전달
                finish();
            }
        });
    }
    public String Checked(View v) { // 체크되었을 때 동작할 메소드 구현
        // TODO Auto-generated method stub
        CheckBox cb_alcohol = (CheckBox) findViewById(R.id.cb_alcohol); // option1체크박스
        // 선언
        CheckBox cb_date = (CheckBox) findViewById(R.id.cb_date); // option1체크박스
        // 선언
        CheckBox cb_meeting = (CheckBox) findViewById(R.id.cb_meeting);
        CheckBox cb_wedding = (CheckBox) findViewById(R.id.cb_wedding);
        CheckBox cb_group = (CheckBox) findViewById(R.id.cb_group);
        CheckBox cb_family = (CheckBox) findViewById(R.id.cb_family);
        CheckBox cb_propose = (CheckBox) findViewById(R.id.cb_propose);

        String resultText = ""; // 체크되었을 때 값을 저장할 스트링 값
        if (cb_alcohol.isChecked()) { // option1 이 체크되었다면
            resultText = "음주";
        }
        if (cb_date.isChecked()) {
            resultText = "데이트"; // option2 이 체크되었다면
        }
        if (cb_meeting.isChecked()) {
            resultText = "소개팅"; // option2 이 체크되었다면
        }
        if (cb_wedding.isChecked()) {
            resultText = "상견례"; // option2 이 체크되었다면
        }
        if (cb_group.isChecked()) {
            resultText = "회사 회식(단체)"; // option2 이 체크되었다면
        }
        if (cb_family.isChecked()) {
            resultText = "가족모임"; // option2 이 체크되었다면
        }
        if (cb_propose.isChecked()) {
            resultText = "프로포즈"; // option2 이 체크되었다면
        }

        return resultText; // 체크된 값 리턴
    }

    public String Checked2(View v) { // 체크되었을 때 동작할 메소드 구현
        // TODO Auto-generated method stub
        CheckBox cb_kfood = (CheckBox) findViewById(R.id.cb_alcohol); // option1체크박스
        // 선언
        CheckBox cb_wfood = (CheckBox) findViewById(R.id.cb_date); // option1체크박스
        // 선언
        CheckBox cb_cfood = (CheckBox) findViewById(R.id.cb_meeting);
        CheckBox cb_jfood = (CheckBox) findViewById(R.id.cb_wedding);
        CheckBox cb_sfood = (CheckBox) findViewById(R.id.cb_group);
        CheckBox cb_ffood = (CheckBox) findViewById(R.id.cb_family);
        CheckBox cb_cafe = (CheckBox) findViewById(R.id.cb_propose);

        String resultText = ""; // 체크되었을 때 값을 저장할 스트링 값
        if (cb_kfood.isChecked()) { // option1 이 체크되었다면
            resultText = "한식";
        }
        if (cb_wfood.isChecked()) {
            resultText = "양식"; // option2 이 체크되었다면
        }
        if (cb_cfood.isChecked()) {
            resultText = "중식"; // option2 이 체크되었다면
        }
        if (cb_jfood.isChecked()) {
            resultText = "일식"; // option2 이 체크되었다면
        }
        if (cb_sfood.isChecked()) {
            resultText = "분식"; // option2 이 체크되었다면
        }
        if (cb_ffood.isChecked()) {
            resultText = "패스트푸드"; // option2 이 체크되었다면
        }
        if (cb_cafe.isChecked()) {
            resultText = "카페"; // option2 이 체크되었다면
        }

        return resultText; // 체크된 값 리턴
    }

    public String Checked3(View v) { // 체크되었을 때 동작할 메소드 구현
        // TODO Auto-generated method stub
        CheckBox cb_magic = (CheckBox) findViewById(R.id.cb_magic); // option1체크박스
        // 선언
        CheckBox cb_gold = (CheckBox) findViewById(R.id.cb_gold); // option1체크박스
        // 선언
        CheckBox cb_diet = (CheckBox) findViewById(R.id.cb_diet);
        CheckBox cb_health = (CheckBox) findViewById(R.id.cb_health);
        CheckBox cb_night = (CheckBox) findViewById(R.id.cb_night);
        CheckBox cb_sick = (CheckBox) findViewById(R.id.cb_sick);
        CheckBox cb_bulkup = (CheckBox) findViewById(R.id.cb_bulkup);
        CheckBox cb_rain = (CheckBox) findViewById(R.id.cb_rain);

        String resultText = ""; // 체크되었을 때 값을 저장할 스트링 값
        if (cb_magic.isChecked()) { // option1 이 체크되었다면
            resultText = "생리전";
        }
        if (cb_gold.isChecked()) {
            resultText = "황금기"; // option2 이 체크되었다면
        }
        if (cb_diet.isChecked()) {
            resultText = "다이어트"; // option2 이 체크되었다면
        }
        if (cb_health.isChecked()) {
            resultText = "몸보신"; // option2 이 체크되었다면
        }
        if (cb_night.isChecked()) {
            resultText = "밤샘"; // option2 이 체크되었다면
        }
        if (cb_sick.isChecked()) {
            resultText = "병"; // option2 이 체크되었다면
        }
        if (cb_bulkup.isChecked()) {
            resultText = "벌크업"; // option2 이 체크되었다면
        }
        if (cb_rain.isChecked()) {
            resultText = "비오는날"; // option2 이 체크되었다면
        }

        return resultText; // 체크된 값 리턴
    }
}
