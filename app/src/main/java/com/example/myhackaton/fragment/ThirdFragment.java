package com.example.myhackaton.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myhackaton.FirebaseDB;
import com.example.myhackaton.L;
import com.example.myhackaton.R;
import com.example.myhackaton.activity.AlarmActivity;
import com.example.myhackaton.activity.FoodViewerActivity;
import com.example.myhackaton.auth.FirebaseAuth;
import com.example.myhackaton.helper.PreferenceHelper;
import com.example.myhackaton.model.Account;
import com.example.myhackaton.model.Booking;
import com.example.myhackaton.model.Sales;
import com.example.myhackaton.model.dto.SalesDto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThirdFragment extends Fragment {
    @BindView(R.id.salesRecyclerView)
    RecyclerView mSalesRecyclerView;
    ReviewAdapter mReviewAdapter;
    //    DatabaseReference mRequestReference;
    Account mAccount;
    PreferenceHelper mPreferenceHelper;
    ArrayList<Booking> mBookingList = new ArrayList<>();

    public ThirdFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_third, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
//        mRequestReference = FirebaseDatabase.getInstance().getReference().child(FirebaseDB.HY_BOOKING);
        mPreferenceHelper = new PreferenceHelper(getAlarmActivity());
        mAccount = mPreferenceHelper.getCurrentAccount();

        initSalesView();
        refreshBooking();
    }

    private void initSalesView() {
        mSalesRecyclerView.setHasFixedSize(true);
        mSalesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mReviewAdapter = new ReviewAdapter();
        mSalesRecyclerView.setAdapter(mReviewAdapter);
    }

    private void refreshBooking() {
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child(FirebaseDB.HY_BOOKING).child(FirebaseAuth.getCurrentUser().getUid());
//        orderByChild("addedByUser").equalTo(FirebaseAuth.getCurrentUser().getUid());
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    return;
                }
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Booking booking = child.getValue(Booking.class);
                    L.i(":::booking : " + booking);
//                    L.i(":::sales seq  : " + child.getKey());
//                    L.i("child.getChildrenCount() = " + child.getChildrenCount());
//                    for (int x = 0; x < child.getChildrenCount(); x++) {
//                        for (DataSnapshot child2 : child.getChildren()) {
//                            Booking booking = child2.getValue(Booking.class);
//                            if (booking.getGuestUniqueKey().equalsIgnoreCase(mAccount.getAddedByUser())) {
                    mBookingList.add(booking);
//                            }
//                            L.e(":::booking : " + booking);
//                            L.e(":::booking seq  : " + child2.getKey());
//                        }
//                    }

                }
                mReviewAdapter.onRefresh(mBookingList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        L.e();
    }

    private AlarmActivity getAlarmActivity() {
        Activity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            AlarmActivity mainActivity = (AlarmActivity) activity;
            return mainActivity;
        }
        return null;
    }

    public static class ReviewHolder extends RecyclerView.ViewHolder {
        TextView storeName;
        TextView menuName;
        TextView saleType;
        TextView salePercent;
        Button mDetail;

        public ReviewHolder(View itemView) {
            super(itemView);
            storeName = itemView.findViewById(R.id.store_name);
            menuName = itemView.findViewById(R.id.menuName);
            saleType = itemView.findViewById(R.id.sale_type);
            salePercent = itemView.findViewById(R.id.sale_percent);
            mDetail = itemView.findViewById(R.id.detail);


        }
    }

    private class ReviewAdapter extends RecyclerView.Adapter<ReviewHolder> {

        ArrayList<Booking> bookingList = new ArrayList<>();

        @Override
        public ReviewHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_guest_main_book_list_item, parent, false);
            return new ReviewHolder(view);
        }


        @Override
        public void onBindViewHolder(ReviewHolder rowViewHolder, final int pos) {
            L.e("pos = " + pos);
            Booking booking = bookingList.get(pos);

            rowViewHolder.storeName.setText(booking.getStoreName());
            rowViewHolder.menuName.setText(booking.getMenu());
            rowViewHolder.saleType.setText(booking.getSaleType());
            rowViewHolder.salePercent.setText(booking.getSalePercent());

            rowViewHolder.mDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(getAlarmActivity(), FoodViewerActivity.class);
//                    ////////

                    getAlarmActivity().setLoading("자세히 보기를 준비중입니다..");
                    DatabaseReference mSalesReference = FirebaseDatabase.getInstance().getReference().child(FirebaseDB.HY_SALES);
                    Query qyery = mSalesReference.orderByChild("saleUniqueKey").equalTo(booking.getSaleUniqueKey());
                    qyery.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {
                                if (getAlarmActivity() != null) {
                                    getAlarmActivity().hideProgressDialog();
                                }
                                return;
                            }

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                Sales sales = child.getValue(Sales.class);
                                L.i(":::sales : " + sales);
                                L.i(":::sales seq  : " + child.getKey());
                                if (sales != null) {
                                    Intent intent = new Intent(getAlarmActivity(), FoodViewerActivity.class);
                                    ////////
                                    ArrayList<SalesDto> list = new ArrayList<>();
                                    list.add(SalesDto.builder().mSeq(child.getKey()).sale(sales).build());
                                    intent.putExtra("sale_third", list);
                                    getAlarmActivity().startActivityWithAni(intent);
                                }
                            }

                            getAlarmActivity().hideProgressDialog();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            });
        }

        @Override
        public int getItemCount() {
            return bookingList.size();
        }

        public void onRefresh(ArrayList<Booking> sales) {
            this.bookingList = sales;
            notifyDataSetChanged();
        }
    }

}

