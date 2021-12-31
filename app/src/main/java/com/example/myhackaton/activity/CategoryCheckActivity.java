package com.example.myhackaton.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myhackaton.L;
import com.example.myhackaton.R;
import com.example.myhackaton.RootActivity;
import com.example.myhackaton.helper.MessageUtils;

public class CategoryCheckActivity extends RootActivity {

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
                Checked3(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_gold).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked3(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_diet).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked3(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_health).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked3(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_night).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked3(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_sick).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked3(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_bulkup).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked3(v); // 체크되었을 때 동작코드
            }
        });
        findViewById(R.id.cb_rain).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Checked3(v); // 체크되었을 때 동작코드
            }
        });

        Button bt_save_category = (Button) findViewById(R.id.bt_save_category);
        bt_save_category.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                //return data
                L.e("resultText1 = "+ resultText1 +",resultText2 = "+ resultText2 + ", resultText3 = "+ resultText2);
                if(TextUtils.isEmpty(resultText1) || TextUtils.isEmpty(resultText2) || TextUtils.isEmpty(resultText3)){
                    MessageUtils.showLongToastMsg(getApplicationContext(), "비어있는 항목을 체크해 주세요.");
                    return;
                }
                Intent intent = new Intent(CategoryCheckActivity.this, PlusActivity.class); // MainActivity.java로  보내기 위한  인텐트 선언
                intent.putExtra("it_check", resultText1); // it_check 라는 이름하에 체크된 값을 전달
                intent.putExtra("it_check2", resultText2);
                intent.putExtra("it_check3", resultText3);
                setResult(Activity.RESULT_OK, intent);
                onBackPressed();
            }
        });
    }
    String resultText1, resultText2, resultText3;
    public void Checked(View v) { // 체크되었을 때 동작할 메소드 구현
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

        CheckBox selected = (CheckBox) v;
        if(selected.isChecked()){
            enableOnlyOne(v, findViewById(R.id.concept_layout));
        }

        resultText1= ""; // 체크되었을 때 값을 저장할 스트링 값
        if (cb_alcohol.isChecked()) { // option1 이 체크되었다면
            resultText1 = "음주";

        }
        if (cb_date.isChecked()) {
            resultText1 = "데이트"; // option2 이 체크되었다면
        }
        if (cb_meeting.isChecked()) {
            resultText1 = "소개팅"; // option2 이 체크되었다면
        }
        if (cb_wedding.isChecked()) {
            resultText1 = "상견례"; // option2 이 체크되었다면
        }
        if (cb_group.isChecked()) {
            resultText1 = "회사 회식(단체)"; // option2 이 체크되었다면
        }
        if (cb_family.isChecked()) {
            resultText1 = "가족모임"; // option2 이 체크되었다면
        }
        if (cb_propose.isChecked()) {
            resultText1 = "프로포즈"; // option2 이 체크되었다면
        }
        L.e(resultText1);
    }

    private synchronized void enableOnlyOne(View v, LinearLayout layout) {
        int childCount = layout.getChildCount();
        for(int x = 0; x < childCount; x++){
            View view = layout.getChildAt(x);
            if(view instanceof CheckBox){
                CheckBox cb = (CheckBox) view;
                if(cb != v){
                    cb.setChecked(false);
                }

            }
        }
    }

    public  void Checked2(View v) { // 체크되었을 때 동작할 메소드 구현
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
        CheckBox selected = (CheckBox) v;
        if(selected.isChecked()){
            enableOnlyOne(v, findViewById(R.id.food_layout));
        }


        if (cb_kfood.isChecked()) { // option1 이 체크되었다면
            resultText2 = "한식";
        }
        if (cb_wfood.isChecked()) {
            resultText2 = "양식"; // option2 이 체크되었다면
        }
        if (cb_cfood.isChecked()) {
            resultText2 = "중식"; // option2 이 체크되었다면
        }
        if (cb_jfood.isChecked()) {
            resultText2 = "일식"; // option2 이 체크되었다면
        }
        if (cb_sfood.isChecked()) {
            resultText2 = "분식"; // option2 이 체크되었다면
        }
        if (cb_ffood.isChecked()) {
            resultText2 = "패스트푸드"; // option2 이 체크되었다면
        }
        if (cb_cafe.isChecked()) {
            resultText2 = "카페"; // option2 이 체크되었다면
        }

        L.e(resultText2);
    }

    public void Checked3(View v) { // 체크되었을 때 동작할 메소드 구현
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
        CheckBox selected = (CheckBox) v;
        if(selected.isChecked()){
            enableOnlyOne(v, findViewById(R.id.condition_layout));
        }

        if (cb_magic.isChecked()) { // option1 이 체크되었다면
            resultText3 = "생리전";
        }
        if (cb_gold.isChecked()) {
            resultText3 = "황금기"; // option2 이 체크되었다면
        }
        if (cb_diet.isChecked()) {
            resultText3 = "다이어트"; // option2 이 체크되었다면
        }
        if (cb_health.isChecked()) {
            resultText3 = "몸보신"; // option2 이 체크되었다면
        }
        if (cb_night.isChecked()) {
            resultText3 = "밤샘"; // option2 이 체크되었다면
        }
        if (cb_sick.isChecked()) {
            resultText3 = "병"; // option2 이 체크되었다면
        }
        if (cb_bulkup.isChecked()) {
            resultText3 = "벌크업"; // option2 이 체크되었다면
        }
        if (cb_rain.isChecked()) {
            resultText3 = "비오는날"; // option2 이 체크되었다면
        }
        L.e(resultText3);
    }
}
