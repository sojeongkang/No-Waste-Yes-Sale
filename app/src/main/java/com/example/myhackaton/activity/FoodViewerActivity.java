package com.example.myhackaton.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.example.myhackaton.model.Booking;
import com.example.myhackaton.model.Sales;
import com.example.myhackaton.model.dto.SalesDto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class FoodViewerActivity extends RootActivity {

    public final static int REQUEST_CODE = 0x06;

    TimePicker mSaleEndTime;

    String mSaleSTime = "";
    String mSaleETime = "";

    Account mAccount;

    @BindView(R.id.sale_duration)
    TextView mSaleDuration;
    @BindView(R.id.menulist)
    TextView mMenu;
    @BindView(R.id.price)
    TextView mPrice;
    @BindView(R.id.sale_percent)
    TextView mSalePercent;
    @BindView(R.id.totalprice)
    TextView mTotalPrice;
    @BindView(R.id.store)
    TextView mStore;
    @BindView(R.id.saleLayout)
    LinearLayout mSaleLayout;
    @BindView(R.id.bt_reservation)
    Button mBookingBtn;

    Sales mSale;
    String mSeq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_foodviewer);
        super.onCreate(savedInstanceState);
        mAccount = mPreferenceHelper.getCurrentAccount();
        if (FirebaseAuth.getCurrentUser() != null) {
            L.e("FirebaseAuth.getCurrentUser().getUid() = " + FirebaseAuth.getCurrentUser().getUid());

        }
        if(getIntent().hasExtra("sale")){
            ArrayList<SalesDto> list = (ArrayList<SalesDto>) getIntent().getSerializableExtra("sale");
            if(list.size() > 0){
                mSeq = list.get(0).getMSeq();
                mSale = list.get(0).getSale();
            }
        } else if(getIntent().hasExtra("sale_third")){
            ArrayList<SalesDto> list = (ArrayList<SalesDto>) getIntent().getSerializableExtra("sale_third");
            if(list.size() > 0){
                mSeq = list.get(0).getMSeq();
                mSale = list.get(0).getSale();
            }
            mBookingBtn.setVisibility(View.GONE);
        }

        initSaleInfo();
    }

    private void initSaleInfo() {
        mStore.setText(mSale.getStoreName());
        mMenu.setText(mSale.getMenu());
        mSaleDuration.setText(mSale.getSSaleTime() +"~" +mSale.getESaleTime());
        mPrice.setText(mSale.getPrice());
        mSalePercent.setText(mSale.getSalePercent());
        mTotalPrice.setText(mSale.getTotalPrice());
        initSaleType();
    }

    private void initSaleType() {
        String result = "";
        int rGroupCount = mSaleLayout.getChildCount();
        for (int pos = 0; pos < rGroupCount; pos++) {
            LinearLayout lLayout = (LinearLayout) mSaleLayout.getChildAt(pos);
            int rCount = lLayout.getChildCount();
            for (int x = 0; x < rCount; x++) {
                RadioButton rButton = (RadioButton) lLayout.getChildAt(x);
                L.i("rButton = "+ rButton);
                if (rButton.getText().toString().equalsIgnoreCase(mSale.getSaleType())) {
                    rButton.setChecked(true);
                }
            }
        }
    }

    @OnClick(R.id.bt_reservation)
    public void onConfirm(View view){
        L.e();

        Booking booking = Booking.builder().addedByHost(mSale.getAddedByUser()).guestUniqueKey(mAccount.getAddedByUser()).storeName(mSale.getStoreName()).menu(mSale.getMenu())
        .saleType(mSale.getSaleType()).saleUniqueKey(mSeq).salePercent(mSale.getSalePercent()).build();

        DatabaseReference mRequestReference = FirebaseDatabase.getInstance().getReference().child(FirebaseDB.HY_BOOKING).child(mAccount.getAddedByUser()).push();
        mRequestReference.setValue(booking).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    MessageUtils.showLongToastMsg(getApplicationContext(), "신청이 완료되었습니다.");
                    setResult(Activity.RESULT_OK);
                    onBackPressed();
                } else {
                    MessageUtils.showLongToastMsg(getApplicationContext(), "알수없는 오류가 발생하였습니다.");
                }
            }
        });
    }

}
