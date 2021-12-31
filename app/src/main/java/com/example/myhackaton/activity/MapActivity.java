package com.example.myhackaton.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myhackaton.R;
import com.example.myhackaton.RootActivity;


public class MapActivity extends RootActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_map);
        super.onCreate(savedInstanceState);

    }

    public void btt_click(View v) {
        Intent intent = new Intent(MapActivity.this, PlusActivity.class); // MainActivity.java로  보내기 위한  인텐트 선언
        if (v.getId() == R.id.bt_young) {
            intent.putExtra("it_click", "용산"); // it_check 라는 이름하에 체크된 값을 전달
        } else if (v.getId() == R.id.bt_hong) {
            intent.putExtra("it_click", "동대문"); // it_check 라는 이름하에 체크된 값을 전달
        } else if (v.getId() == R.id.bt_sin) {
            intent.putExtra("it_click", "신촌"); // it_check 라는 이름하에 체크된 값을 전달
        } else if (v.getId() == R.id.bt_gun) {
            intent.putExtra("it_click", "건대"); // it_check 라는 이름하에 체크된 값을 전달
        } else if (v.getId() == R.id.bt_mung) {
            intent.putExtra("it_click", "명동"); // it_check 라는 이름하에 체크된 값을 전달
        } else if (v.getId() == R.id.bt_insa) {
            intent.putExtra("it_click", "인사동"); // it_check 라는 이름하에 체크된 값을 전달
        } else if (v.getId() == R.id.bt_jung) {
            intent.putExtra("it_click", "중구"); // it_check 라는 이름하에 체크된 값을 전달
        } else if (v.getId() == R.id.bt_lee) {
            intent.putExtra("it_click", "이태원"); // it_check 라는 이름하에 체크된 값을 전달
        } else if (v.getId() == R.id.bt_enp) {
            intent.putExtra("it_click", "은평"); // it_check 라는 이름하에 체크된 값을 전달
        } else if (v.getId() == R.id.bt_jong) {
            intent.putExtra("it_click", "종로"); // it_check 라는 이름하에 체크된 값을 전달
        } else if (v.getId() == R.id.bt_dehak) {
            intent.putExtra("it_click", "대학로"); // it_check 라는 이름하에 체크된 값을 전달
        } else if (v.getId() == R.id.bt_sam) {
            intent.putExtra("it_click", "삼천동"); // it_check 라는 이름하에 체크된 값을 전달
        } else if (v.getId() == R.id.bt_kang) {
            intent.putExtra("it_click", "강북/성북"); // it_check 라는 이름하에 체크된 값을 전달
        } else if (v.getId() == R.id.bt_dong) {
            intent.putExtra("it_click", "홍대"); // it_check 라는 이름하에 체크된 값을 전달
        } else if (v.getId() == R.id.bt_hap) {
            intent.putExtra("it_click", "동대문/성동"); // it_check 라는 이름하에 체크된 값을 전달

        }
        setResult(Activity.RESULT_OK, intent);
        onBackPressed();

    }


    public void bt_sin(View v) {
        Intent intent = new Intent(getApplicationContext(), PlusActivity.class);
        startActivity(intent);
    }

    public void bt_gun(View v) {
        Intent intent = new Intent(getApplicationContext(), PlusActivity.class);
        startActivity(intent);
    }

    public void bt_mung(View v) {
        Intent intent = new Intent(getApplicationContext(), PlusActivity.class);
        startActivity(intent);
    }

    public void bt_lee(View v) {
        Intent intent = new Intent(getApplicationContext(), PlusActivity.class);
        startActivity(intent);
    }

    public void bt_hap(View v) {
        Intent intent = new Intent(getApplicationContext(), PlusActivity.class);
        startActivity(intent);
    }
}
