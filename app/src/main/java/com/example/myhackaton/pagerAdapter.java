package com.example.myhackaton;

import androidx.fragment.app.FragmentStatePagerAdapter;

public class pagerAdapter extends FragmentStatePagerAdapter {
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
    public CharSequence getPageTitle(int position)
    {
        switch(position)
        {
            case 0:
                return "캘린더";
            case 1:
                return "현위치";
            case 2:
                return "기타";
            default:
                return null;
        }
    }
}


