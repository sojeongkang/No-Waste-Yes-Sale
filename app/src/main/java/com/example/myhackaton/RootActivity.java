package com.example.myhackaton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myhackaton.activity.FoodViewerActivity;
import com.example.myhackaton.helper.AnimationDirection;
import com.example.myhackaton.helper.PreferenceHelper;
import com.example.myhackaton.model.dto.SalesDto;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class RootActivity extends AppCompatActivity {

    public PreferenceHelper mPreferenceHelper;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mPreferenceHelper = new PreferenceHelper(getApplicationContext());
    }

    public void setLoading(String title) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(title);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
    }


    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishWithAni(AnimationDirection.Direction.EXIT_RIGHT);
    }

    public void startActivityWithAni(Intent intent) {
        AnimationDirection.Direction direction = AnimationDirection.Direction.ENTER_RIGHT;
        startActivity(intent);
        overridePendingTransition(direction.getEnterAni(), direction.getExitAni());

    }

    public void startActivityWithAniForResult(Intent intent, int requesCode) {
        AnimationDirection.Direction direction = AnimationDirection.Direction.ENTER_RIGHT;
        startActivityForResult(intent, requesCode);
        overridePendingTransition(direction.getEnterAni(), direction.getExitAni());

    }

    public void finishWithAni(AnimationDirection.Direction direction) {
        finish();
        overridePendingTransition(direction.getEnterAni(), direction.getExitAni());
    }

}
