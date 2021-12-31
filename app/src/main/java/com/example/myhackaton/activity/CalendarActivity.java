package com.example.myhackaton.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myhackaton.FirebaseDB;
import com.example.myhackaton.L;
import com.example.myhackaton.R;
import com.example.myhackaton.RootActivity;
import com.example.myhackaton.helper.MessageUtils;
import com.example.myhackaton.model.Account;
import com.example.myhackaton.model.Schedule;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;

public class CalendarActivity extends RootActivity {
    @BindView(R.id.list_schedule)
    RecyclerView mSalesRecyclerView;

    @BindView(R.id.calendarView)
    CalendarView mCalendar;
    ReviewAdapter mReviewAdapter;
    Account mAccount;
    ArrayList<Schedule> mScheduleList = new ArrayList<>();
    private boolean mIsFirstLaunch = true;

    SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_calendar);
        super.onCreate(savedInstanceState);
        mAccount = mPreferenceHelper.getCurrentAccount();
        initCalendar();
        initScheduleView();
        getSchedule(mDateFormat.format(mSelectedCalendar.getTime()));
    }

    private Calendar mSelectedCalendar = Calendar.getInstance();
    private void initCalendar() {
        mCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {
                L.e("year = " + year + "/" + (month + 1) + "/" + dayOfMonth);
                mSelectedCalendar.set(Calendar.YEAR, year);
                mSelectedCalendar.set(Calendar.MONTH, month);
                mSelectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String dateStr = mDateFormat.format(mSelectedCalendar.getTime());
                setLoading(dateStr+ "의 스케쥴을 로드 중입니다.");
                getSchedule(dateStr);

            }
        });

    }

    private void initScheduleView() {
        mSalesRecyclerView.setHasFixedSize(true);
        mSalesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mReviewAdapter = new ReviewAdapter();
        mSalesRecyclerView.setAdapter(mReviewAdapter);
    }

    public void bt_plus(View v) {
        Intent intent = new Intent(getApplicationContext(), PlusActivity.class);
        intent.putExtra("current_date", mSelectedCalendar.getTimeInMillis());
        startActivityWithAniForResult(intent, PlusActivity.REQUEST_CODE);
    }

    public void bt_before(View v) {
        Intent intent = new Intent(getApplicationContext(), Guest_mainActivity.class);
        onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e("requestCode  = " + requestCode + ", resultCode = " + resultCode);
        if (requestCode == PlusActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //화면 갱신
            setLoading("스케쥴 목록을 불러오는 중입니다.");
            getSchedule(mDateFormat.format(mSelectedCalendar.getTimeInMillis()));
        }
    }

    private void getSchedule(String date) {

        if (mIsFirstLaunch) {
            mIsFirstLaunch = false;
            setLoading("스케쥴 정보를 불러오는 중입니다.");
        }
        Query query = FirebaseDatabase.getInstance().getReference().child(FirebaseDB.HY_SCHEDULE).orderByChild("addedByUser").equalTo(mAccount.getAddedByUser());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    hideProgressDialog();
                    return;
                }

                ArrayList<Schedule> list = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Schedule schedule = child.getValue(Schedule.class);
                    L.i(":::schedule : " + schedule);
                    L.i(":::schedule seq  : " + child.getKey());
                    if (schedule != null) {
                        if(schedule.getDate().equalsIgnoreCase(date))
                        list.add(schedule);
                    }
                }
                hideProgressDialog();
                mReviewAdapter.onRefresh(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//            }
//        });
    }

    public static class ReviewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView time;
        TextView category;
        TextView region;
        TextView place;

        public ReviewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            category = itemView.findViewById(R.id.category);
            region = itemView.findViewById(R.id.region);
            place = itemView.findViewById(R.id.place);

        }
    }

    class ReviewAdapter extends RecyclerView.Adapter<ReviewHolder> {

        ArrayList<Schedule> schedules = new ArrayList<>();

        @Override
        public ReviewHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_guest_main_schedule_list_item, parent, false);
            return new ReviewHolder(view);
        }


        @Override
        public void onBindViewHolder(ReviewHolder rowViewHolder, final int pos) {
            L.e("pos = " + pos);
            Schedule schedule = schedules.get(pos);
            rowViewHolder.title.setText(schedule.getSubject());
            rowViewHolder.time.setText(schedule.getStartTime() + " ~ " + schedule.getEndTime());
            rowViewHolder.category.setText(schedule.getConcept());
            rowViewHolder.region.setText(schedule.getRegion());
            rowViewHolder.place.setText(schedule.getPlace());
        }

        @Override
        public int getItemCount() {
            return schedules.size();
        }

        public void onRefresh(ArrayList<Schedule> list) {
            this.schedules = list;
            notifyDataSetChanged();
        }
    }
}
