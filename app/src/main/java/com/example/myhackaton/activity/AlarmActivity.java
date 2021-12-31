package com.example.myhackaton.activity;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myhackaton.L;
import com.example.myhackaton.R;
import com.example.myhackaton.RootActivity;
import com.example.myhackaton.fragment.SecondFragment;
import com.example.myhackaton.fragment.ThirdFragment;
import com.google.android.material.tabs.TabLayout;

public class AlarmActivity extends RootActivity {
    public final static int REQUEST_CODE = 0x05;
    ViewPager vp;
    View.OnClickListener movePageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();
            vp.setCurrentItem(tag);
        }
    };

    private boolean mIsFromHostJoin = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_alarm);
        super.onCreate(savedInstanceState);


        if (getIntent().hasExtra("isFromHostJoin")) {
            mIsFromHostJoin = true;
        }
        vp = findViewById(R.id.vp);
        vp.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);

        TabLayout mTab = findViewById(R.id.tabs);
        mTab.setupWithViewPager(vp);

        if (mIsFromHostJoin) {
            mTab.setVisibility(View.GONE);
        }
    }

    public boolean getIsFromHostJoin(){
        return mIsFromHostJoin;
    }

    private class pagerAdapter extends FragmentStatePagerAdapter {
        public pagerAdapter(androidx.fragment.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public androidx.fragment.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FirstFragment();
                case 1:
                    return new SecondFragment();
                case 2:
                    return new ThirdFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "GPS";
                case 1:
                    return "Calendar";
                case 2:
                    return "my reservation";
                default:
                    return null;
            }
        }
    }
}
