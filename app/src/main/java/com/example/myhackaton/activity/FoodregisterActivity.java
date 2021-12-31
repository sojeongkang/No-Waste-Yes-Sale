package com.example.myhackaton.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.example.myhackaton.FirebaseDB;
import com.example.myhackaton.L;
import com.example.myhackaton.R;
import com.example.myhackaton.RootActivity;
import com.example.myhackaton.auth.FirebaseAuth;
import com.example.myhackaton.helper.MessageUtils;
import com.example.myhackaton.model.Account;
import com.example.myhackaton.model.Sales;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

public class FoodregisterActivity extends RootActivity {

    public final static int REQUEST_CODE = 0x03;

    @BindView(R.id.menulist)
    Spinner mMenuSpinner;
    @BindView(R.id.saleLayout)
    LinearLayout mSaleLayout;

    @BindView(R.id.rb_noshow)
    RadioButton mNoShow;
    @BindView(R.id.rb_timesale)
    RadioButton mTimeSale;
    @BindView(R.id.rb_takeout)
    RadioButton mTakeOut;
    @BindView(R.id.rb_etc)
    RadioButton mEtc;
    @BindView(R.id.price)
    EditText mPrice;
    @BindView(R.id.totalprice)
    TextView mTotalPrice;
    @BindView(R.id.spinner_salepersent)
    Spinner mSalePercent;
    @BindView(R.id.timePicker_salestart)
    TimePicker mSaleStartTime;
    @BindView(R.id.timePicker_saleend)
    TimePicker mSaleEndTime;

    String mSaleSTime = "";
    String mSaleETime = "";

    Account mAccount;
    private CompoundButton.OnCheckedChangeListener mSaleTypeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            L.e("compoundButton = " + compoundButton + ",isChecked = " + isChecked);
            if (compoundButton == mNoShow) {
                if (isChecked) {
                    mTimeSale.setChecked(false);
                    mTakeOut.setChecked(false);
                    mEtc.setChecked(false);
                }
            } else if (compoundButton == mTimeSale) {
                if (isChecked) {
                    mNoShow.setChecked(false);
                    mTakeOut.setChecked(false);
                    mEtc.setChecked(false);
                }
            } else if (compoundButton == mTakeOut) {
                if (isChecked) {
                    mNoShow.setChecked(false);
                    mTimeSale.setChecked(false);
                    mEtc.setChecked(false);
                }
            } else {
                if (isChecked) {
                    mNoShow.setChecked(false);
                    mTimeSale.setChecked(false);
                    mTakeOut.setChecked(false);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_foodregister);
        super.onCreate(savedInstanceState);
        mAccount = mPreferenceHelper.getCurrentAccount();
        if (FirebaseAuth.getCurrentUser() != null) {
            L.e("FirebaseAuth.getCurrentUser().getUid() = "+ FirebaseAuth.getCurrentUser().getUid());

        }
        initSpinner();
        initSaleType();
        initTimePicker();
    }

    private void initTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        L.e("hourOfDay = "+ hourOfDay +", min = "+ min);
        mSaleStartTime.setCurrentHour(hourOfDay);
        mSaleStartTime.setCurrentMinute(min);
        mSaleEndTime.setCurrentHour(hourOfDay);
        mSaleEndTime.setCurrentMinute(min);
        setStartSalesTime(hourOfDay, min);
        setEndSalesTime(hourOfDay, min);

        mSaleStartTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                L.e("hourOfDay = " + hourOfDay + ", minute = " + minute);
                setStartSalesTime(hourOfDay, minute);

            }
        });
        mSaleEndTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                L.e("hourOfDay = " + hourOfDay + ", minute = " + minute);
                setEndSalesTime(hourOfDay, minute);

            }
        });


    }

    private void setEndSalesTime(int hourOfDay, int minute) {
        StringBuilder sb = new StringBuilder();
        sb.append(hourOfDay).append("시").append("-").append(minute).append("분");
        mSaleETime = sb.toString();
        L.e("mSaleETime = " + mSaleETime);
    }

    private void setStartSalesTime(int hourOfDay, int minute) {
        StringBuilder sb = new StringBuilder();
        sb.append(hourOfDay).append("시").append("-").append(minute).append("분");
        mSaleSTime = sb.toString();
        L.e("mSaleSTime = " + mSaleSTime);
    }

    private void initSaleType() {
        mNoShow.setOnCheckedChangeListener(mSaleTypeListener);
        mTimeSale.setOnCheckedChangeListener(mSaleTypeListener);
        mTakeOut.setOnCheckedChangeListener(mSaleTypeListener);
        mEtc.setOnCheckedChangeListener(mSaleTypeListener);

    }


    private void initSpinner() {
        //////////////// menu
        String menuList = mAccount.getMenuList();
        String[] menuArray = menuList.split(",");
        ArrayAdapter spinnerCity = new ArrayAdapter(this, android.R.layout.simple_spinner_item, menuArray);
        mMenuSpinner.setAdapter(spinnerCity);


        //////////////// sale percent
        mSalePercent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                Spinner spinner = (Spinner) adapterView;
                String saleValue = (String) spinner.getSelectedItem();
                L.e("saleValue = " + saleValue);
                if (mPrice.getText().toString().trim().isEmpty()) {
                    MessageUtils.showLongToastMsg(getApplicationContext(), "가격을 먼저 입력해 주세요");
                } else {
                    int price = Integer.parseInt(mPrice.getText().toString().trim());
                    mTotalPrice.setText(String.valueOf((price - price * Integer.parseInt(saleValue) / 100)));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    @OnClick(R.id.bt_reservation)
    public void onConfirm(View view) {
        String menu = mMenuSpinner.getSelectedItem().toString();
        String saleType = getSaleType();
        if (saleType.isEmpty()) {
            MessageUtils.showLongToastMsg(getApplicationContext(), "할인 종류를 선택해 주세요.");
            return;
        }

        if (mPrice.getText().toString().isEmpty()) {
            MessageUtils.showLongToastMsg(getApplicationContext(), "정가를 입력해 주세요..");
            return;
        }
        String price = mPrice.getText().toString();
        if (mSalePercent.getSelectedItem().toString().isEmpty()) {
            MessageUtils.showLongToastMsg(getApplicationContext(), "할인율을 선택해 주세요");
            return;
        }
        String salePercent = mSalePercent.getSelectedItem().toString();
        String totalPrice = mTotalPrice.getText().toString();
        L.e("menu = " + menu);
        L.e("saleType = " + saleType);
        L.e("mPrice = " + price);
        L.e("sale percent = " + salePercent);
        L.e("mSaleSTime = " + mSaleSTime);
        L.e("mSaleETime = " + mSaleETime);
        L.e("total price = " + mTotalPrice.toString());

        if(totalPrice.trim().isEmpty()){
            MessageUtils.showLongToastMsg(getApplicationContext(), "");
            return;
        }

        String seq = FirebaseDatabase.getInstance().getReference().child(FirebaseDB.HY_SALES).push().getKey();
        Sales sales = Sales.builder().addedByUser(FirebaseAuth.getCurrentUser().getUid()).storeLocation(mAccount.getStoreLocation()).menu(menu).saleType(saleType).price(price).salePercent(salePercent)
                .sSaleTime(mSaleSTime).eSaleTime(mSaleETime).totalPrice(totalPrice).saleUniqueKey(seq).storeName(mAccount.getStoreName()).build();


        DatabaseReference  salesRef = FirebaseDatabase.getInstance().getReference().child(FirebaseDB.HY_SALES).child(seq);
        salesRef.setValue(sales).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    MessageUtils.showLongToastMsg(getApplicationContext(), "할인상품 등록이 완료되었습니다.");
                    setResult(Activity.RESULT_OK);
                }else{
                    MessageUtils.showLongToastMsg(getApplicationContext(), "할인상품 등록이 실패하였습니다.");
                }
                onBackPressed();
            }
        });
    }

    private String getSaleType() {
        String result = "";
        int rGroupCount = mSaleLayout.getChildCount();
        L.e("rGroupCount = "+ rGroupCount);
        for (int pos = 0; pos < rGroupCount; pos++) {
            LinearLayout lLayout = (LinearLayout) mSaleLayout.getChildAt(pos);
            int rCount = lLayout.getChildCount();
            L.e("pos = "+ pos +", rCount = "+ rCount);
            for (int x = 0; x < rCount; x++) {
                L.e("pos = "+ pos);
                RadioButton rButton = (RadioButton) lLayout.getChildAt(x);
                L.e("rButton.isChecked() = "+ rButton.isChecked());
                if (rButton.isChecked()) {
                    result = rButton.getText().toString().trim();
                    L.e("search sale type result = " + result);
                    return result;
                }
            }
        }
        return "";
    }

    public void bt_food_upload(View v) {
        Intent intent = new Intent(getApplicationContext(), Host_mainActivity.class);
        startActivity(intent);
    }

}
