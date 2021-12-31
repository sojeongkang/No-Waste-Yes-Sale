package com.example.myhackaton.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myhackaton.FirebaseDB;
import com.example.myhackaton.L;
import com.example.myhackaton.R;
import com.example.myhackaton.RootActivity;
import com.example.myhackaton.helper.MessageUtils;
import com.example.myhackaton.model.Account;
import com.example.myhackaton.model.Schedule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

public class PlusActivity extends RootActivity {
    public final static int REQUEST_CODE = 0x08;
    @BindView(R.id.et_title)
    EditText et_title;
    @BindView(R.id.timePicker_plan_start)
    TimePicker timePicker_plan_start;
    @BindView(R.id.timePicker_plan_end)
    TimePicker timePicker_plan_end;

    @BindView(R.id.conceptText)
    TextView conceptText;
    @BindView(R.id.tv_category)
    TextView tv_category;
    @BindView(R.id.tv_foodkind)
    TextView tv_foodkind;
    @BindView(R.id.tv_condition)
    TextView tv_condition;
    @BindView(R.id.tv_region)
    EditText tv_region;
    @BindView(R.id.place)
    EditText place;
    @BindView(R.id.tv_friend)
    TextView tv_friend;
    @BindView(R.id.memo)
    EditText memo;
    @BindView(R.id.bt_save_plus)
    Button bt_save_plus;
    @BindView(R.id.bt_category)
    Button bt_category;
    @BindView(R.id.bt_region)
    Button bt_region;
    @BindView(R.id.bt_friend)
    Button bt_friend;

    Account mAccount;

    long mCurrentTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_plus);
        super.onCreate(savedInstanceState);
        mCurrentTime = getIntent().getLongExtra("current_date", 0L);
        mAccount = mPreferenceHelper.getCurrentAccount();
        bt_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlusActivity.this, CategoryCheckActivity.class);
                startActivityWithAniForResult(intent, 1000);
            }
        });

        Intent it1 = getIntent();      //인텐트 받기 선언
        Intent it2 = getIntent();
        Intent it3 = getIntent();

        //이전에 보냈던 it_check의 값을 받아 str에 저장
        String str = it1.getStringExtra("it_check");    //즉 str = "option1" 또는 "option2" 가 들어있음
        tv_category.setText(str);

        String str2 = it2.getStringExtra("it_check2");
        tv_foodkind.setText(str2);

        String str3 = it3.getStringExtra("it_check3");
        tv_condition.setText(str3);

        bt_region.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlusActivity.this, MapActivity.class);
                startActivityWithAniForResult(intent, 2000);
            }
        });

        Intent itt1 = getIntent();

        String strr1 = itt1.getStringExtra("it_click");
        tv_region.setText(strr1);

        tv_friend = findViewById(R.id.tv_friend);

        bt_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlusActivity.this, FriendActivity.class);
                startActivityWithAniForResult(intent, 3000);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1000) {
                String category = data.getStringExtra("it_check");
                tv_category.setText(category);
                String foodType = data.getStringExtra("it_check2");
                tv_foodkind.setText(foodType);
                String condition = data.getStringExtra("it_check3");
                tv_condition.setText(condition);
            } else if (requestCode == 2000) {
                String mapName = data.getStringExtra("it_click");
                tv_region.setText(mapName);
            } else if (requestCode == 3000) {
                String friend = data.getStringExtra("friend1");
                tv_friend.setText(friend);

            }
        }
    }

    @OnClick(R.id.bt_save_plus)
    public void bt_save_plus(View view) {
//        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
//        startActivity(intent);
        String title = et_title.getText().toString();
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timePicker_plan_start.getCurrentHour());
        calendar.set(Calendar.MINUTE, timePicker_plan_start.getCurrentMinute());

        String startTime = format1.format(calendar.getTimeInMillis());
        calendar.set(Calendar.HOUR_OF_DAY, timePicker_plan_end.getCurrentHour());
        calendar.set(Calendar.MINUTE, timePicker_plan_end.getCurrentMinute());

        String endTime = format1.format(calendar.getTimeInMillis());
        String concept = conceptText.getText().toString();
        String category = tv_category.getText().toString();
        String foodKind = tv_foodkind.getText().toString();
        String condition = tv_condition.getText().toString();
        String region = tv_region.getText().toString();
        String places = place.getText().toString();
        String friend = tv_friend.getText().toString();
        String memos = memo.getText().toString();

        if (title.isEmpty()) title = "-";
        if (startTime.isEmpty()) startTime = "-";
        if (endTime.isEmpty()) endTime = "-";
        if (concept.isEmpty()) concept = "-";
        if (category.isEmpty()) category = "-";
        if (foodKind.isEmpty()) foodKind = "-";
        if (condition.isEmpty()) condition = "-";
        if (region.isEmpty()) region = "-";
        if (places.isEmpty()) places = "-";
        if (memos.isEmpty()) memos = "-";

        setLoading("스케쥴 저장 중입니다.");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(mCurrentTime);
        Schedule schedule = Schedule.builder().addedByUser(mAccount.getAddedByUser()).subject(title).startTime(startTime).endTime(endTime).concept(concept)
                .foodType(foodKind).concept(condition).region(region).place(places).withFriend(friend).date(dateStr)
                .memo(memos).build();
        L.e("schedule = " + schedule);
        String seq = FirebaseDatabase.getInstance().getReference().child(FirebaseDB.HY_SCHEDULE).push().getKey();
        FirebaseDatabase.getInstance().getReference().child(FirebaseDB.HY_SCHEDULE).child(seq).setValue(schedule).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    MessageUtils.showLongToastMsg(getApplicationContext(), "스케쥴 저장이 완료되었습니다.");
                    setResult(Activity.RESULT_OK);
                } else {
                    MessageUtils.showLongToastMsg(getApplicationContext(), "스케쥴 저장이 실패하였습니다.");
                }
                hideProgressDialog();
                onBackPressed();
            }
        });

    }

}
