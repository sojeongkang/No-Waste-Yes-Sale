package com.example.myhackaton.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myhackaton.FirebaseDB;
import com.example.myhackaton.L;
import com.example.myhackaton.R;
import com.example.myhackaton.RootActivity;
import com.example.myhackaton.auth.FirebaseAuth;
import com.example.myhackaton.helper.MessageUtils;
import com.example.myhackaton.helper.PreferenceHelper;
import com.example.myhackaton.model.Account;
import com.example.myhackaton.model.Sales;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class Host_mainActivity extends RootActivity {

    Account mAccount;
    @BindView(R.id.hosthello)
    TextView mHostTv;
    @BindView(R.id.menu)
    EditText mMenuEt;
    @BindView(R.id.address)
    EditText mStoreAddress;
    @BindView(R.id.store)
    EditText mStoreName;
    @BindView(R.id.salesRecyclerView)
    RecyclerView mSalesRecyclerView;
    ReviewAdapter mReviewAdapter;
//    private DatabaseReference mSalesReference = null;
    private ArrayList<Sales> mSalesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_host_main);
        super.onCreate(savedInstanceState);

        mPreferenceHelper = new PreferenceHelper(getApplicationContext());
        mAccount = mPreferenceHelper.getCurrentAccount();
//        mSalesReference = FirebaseDatabase.getInstance().getReference().child(FirebaseDB.HY_SALES);
        initHostInfo();
        initSalesView();

        getSales();
    }

    private void initSalesView() {
        mSalesRecyclerView.setHasFixedSize(true);
        mSalesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mReviewAdapter = new ReviewAdapter();
        mSalesRecyclerView.setAdapter(mReviewAdapter);
    }

    private void initHostInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(mAccount.getEmail().split("@")[0]);
        sb.append("님 안녕하세요.");
        mHostTv.setText(sb.toString());
        mMenuEt.setText(mAccount.getMenuList());
        mStoreAddress.setText(mAccount.getDetailAddress());
        mStoreName.setText(mAccount.getStoreName());
    }


    @OnClick(R.id.bt_sale_plus)
    public void onSalePlus(View view) {
        Intent intent = new Intent(this, FoodregisterActivity.class);
        startActivityWithAniForResult(intent, FoodregisterActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e("resultCode = " + resultCode + ", requestCode= " + requestCode);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FoodregisterActivity.REQUEST_CODE) {
                setLoading("할인정보 가져오는 중....");
                getSales();
            }
        }

    }

    private void getSales() {
        mSalesList.clear();
        L.e("FirebaseAuth.getCurrentUser().getUid() = "+FirebaseAuth.getCurrentUser().getUid());
        Query query =  FirebaseDatabase.getInstance().getReference().child(FirebaseDB.HY_SALES).orderByChild("addedByUser").equalTo(FirebaseAuth.getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    hideProgressDialog();
                    return;
                }
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Sales sales = child.getValue(Sales.class);
                    L.i(":::sales : " + sales);
                    L.i(":::sales seq  : " + child.getKey());
                    if (sales != null) {
                        mSalesList.add(sales);
                        //선택한 신청항목 키값을 이용하여 항목 삭제
                    }
                }

                mReviewAdapter.onRefresh(mSalesList);
                hideProgressDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.bt_logout)
    public void onLogout(View view) {
        if (FirebaseAuth.getCurrentUser() == null) {
            MessageUtils.showLongToastMsg(getApplicationContext(), "계정 정보가 존재하지 않습니다.");
            return;
        }

        com.google.firebase.auth.FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(Host_mainActivity.this, MainActivity.class);
        startActivityWithAni(intent);
        onBackPressed();
        mPreferenceHelper.setCurrentAccount(null);

//        FirebaseDatabase.getInstance().getReference().child(FirebaseDB.HY_ACCOUNTS).child(FirebaseAuth.getCurrentUser().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                FirebaseAuth.delete(new OnResultListener() {
//                    @Override
//                    public void onComplete(Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            MessageUtils.showLongToastMsg(getApplicationContext(), "계정 정보가 삭제되었습니다. 로그인 화면으로 이동합니다.");
//                            Intent intent = new Intent(Host_mainActivity.this, MainActivity.class);
//                            startActivityWithAni(intent);
//                            onBackPressed();
//                        }else{
//                            MessageUtils.showLongToastMsg(getApplicationContext(), "계정 정보 삭제가 실패하였습니다.");
//
//                        }
//                    }
//
//                    @Override
//                    public void onDelete(Task<Void> task) {
//                        if (task != null) {
//                            onBackPressed();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Exception e) {
//
//                    }
//                });
//            }
//        });


    }

    public static class ReviewHolder extends RecyclerView.ViewHolder {
        TextView saleDate;
        TextView menuName;
        TextView saleProduct;

        public ReviewHolder(View itemView) {
            super(itemView);
            saleDate = itemView.findViewById(R.id.saleDate);
            menuName = itemView.findViewById(R.id.menuName);
            saleProduct = itemView.findViewById(R.id.saleProduct);

        }
    }

    private class ReviewAdapter extends RecyclerView.Adapter<ReviewHolder> {

        ArrayList<Sales> sales = new ArrayList<>();

        @Override
        public ReviewHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_host_main_sale_list_item, parent, false);
            return new ReviewHolder(view);
        }


        @Override
        public void onBindViewHolder(ReviewHolder rowViewHolder, final int pos) {
            L.e("pos = "+ pos);
            Sales sale = sales.get(pos);
            rowViewHolder.saleDate.setText(sale.getSSaleTime() +" ~ " +sale.getESaleTime());
            rowViewHolder.menuName.setText(sale.getMenu());
            rowViewHolder.saleProduct.setText(sale.getSaleType());
        }

        @Override
        public int getItemCount() {
            return sales.size();
        }

        public void onRefresh(ArrayList<Sales> sales) {
            this.sales = sales;
            notifyDataSetChanged();
        }
    }
}