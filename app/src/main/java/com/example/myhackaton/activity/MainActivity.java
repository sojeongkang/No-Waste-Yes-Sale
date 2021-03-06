package com.example.myhackaton.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myhackaton.FirebaseDB;
import com.example.myhackaton.L;
import com.example.myhackaton.R;
import com.example.myhackaton.RootActivity;
import com.example.myhackaton.auth.FirebaseAuth;
import com.example.myhackaton.auth.OnResultListener;
import com.example.myhackaton.helper.MessageUtils;
import com.example.myhackaton.model.Account;
import com.example.myhackaton.model.dto.AccountDto;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends RootActivity {


    @BindView(R.id.login_id_edit_text)
    EditText etEmail;

    @BindView(R.id.login_pw_edit_text)
    EditText etPassword;
    @BindView(R.id.Rgroup1)
    RadioGroup rGroup1;
    @BindView(R.id.Hostbutton)
    RadioButton hostbutton;
    @BindView(R.id.Guestbutton)
    RadioButton guestbutton;
    @BindView(R.id.bt_join)
    Button Newjoin;
    @BindView(R.id.bt_login)
    Button Newlogin;
    private ValueEventListener mLoginValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                Account user = dataSnapshot.getValue(Account.class);
                if (user != null) {
                    mPreferenceHelper.setCurrentAccount(user);
                    hideProgressDialog();
                    MessageUtils.showLongToastMsg(getApplicationContext(), "???????????? ?????????????????????.");

                    switch (rGroup1.getCheckedRadioButtonId()) {
                        case R.id.Hostbutton:
                            mPreferenceHelper.setIsHost(true);
                            Intent intent = new Intent(getApplicationContext(), Host_mainActivity.class);
                            startActivityWithAni(intent);
                            onBackPressed();
                            break;
                        case R.id.Guestbutton:
                            mPreferenceHelper.setIsHost(false);
                            Intent intent2 = new Intent(getApplicationContext(), Guest_mainActivity.class);
                            startActivityWithAni(intent2);
                            onBackPressed();
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "Account, Guest ?????? ???????????????", Toast.LENGTH_SHORT)
                                    .show();
                    }
                }
            } else {
                //????????? ????????? ????????????
                hideProgressDialog();
                MessageUtils.showLongToastMsg(getApplicationContext(), "???????????? ?????? ???????????????..");
            }
        }


        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e("resultCode = "+ resultCode +", requestCode= "+ requestCode);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Host_joinActivity.REQUEST_CODE || requestCode == Guest_joinActivity.REQUEST_CODE){
                L.e("?????? ?????? ??????");
                finish();
            }
        }
    }

    @OnClick(R.id.bt_join)
    public void doJoin(View view) {

        switch (rGroup1.getCheckedRadioButtonId()) {
            case R.id.Hostbutton:
                L.e("Host");
                Intent intent = new Intent(getApplicationContext(), Host_joinActivity.class);
                startActivityWithAniForResult(intent, Host_joinActivity.REQUEST_CODE);
                break;
            case R.id.Guestbutton:
                L.e("Guest");
                Intent intent2 = new Intent(getApplicationContext(), Guest_joinActivity.class);
                startActivityWithAniForResult(intent2, Guest_joinActivity.REQUEST_CODE);
                break;
            default:
                Toast.makeText(getApplicationContext(), "Account, Guest ?????? ???????????????", Toast.LENGTH_SHORT)
                        .show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        if(mPreferenceHelper.getCurrentAccount() != null){
            L.e("account type = "+ mPreferenceHelper.getCurrentAccount().getAccountType());
            if(mPreferenceHelper.getCurrentAccount().getAccountType().equalsIgnoreCase(FirebaseDB.HY_USER_HOST)){
                Intent intent = new Intent(getApplicationContext(), Host_mainActivity.class);
                startActivityWithAni(intent);
                onBackPressed();
            }else{
                Intent intent2 = new Intent(getApplicationContext(), Guest_mainActivity.class);
                startActivityWithAni(intent2);
                onBackPressed();
            }
        }
    }

    private void hideKeyboard() {
        //????????? ???????????? ????????????.
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            String serviceName = Context.INPUT_METHOD_SERVICE;
            InputMethodManager imm = (InputMethodManager) getSystemService(serviceName);
            int stateHide = InputMethodManager.HIDE_NOT_ALWAYS;
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), stateHide);
        }
    }


    private boolean isCheckedAccountType(){
        int accountCnt = rGroup1.getChildCount();
        for(int pos =0; pos < accountCnt; pos++){
            RadioButton rButton = (RadioButton) rGroup1.getChildAt(pos);
            if(rButton.isChecked()){
                return true;
            }
        }
        return false;
    }

    @OnClick(R.id.bt_login)
    public void doLogin(View view) {
        //???????????? ????????????.
        //????????? ????????? ????????????.
        hideKeyboard();
        //???????????? ??????????????? Toast ???????????? ???????????? ????????????????????????.
        if (TextUtils.isEmpty(etEmail.getText().toString()) || etEmail.getText().toString().equalsIgnoreCase("")) {
            etEmail.setError("Required");
            MessageUtils.showLongToastMsg(getApplicationContext(), "ID??? ????????? ?????????");
            return;
        }
        etEmail.setError(null);

        //???????????? ??????????????? Toast ???????????? ???????????? ????????????????????????.
        if (TextUtils.isEmpty(etPassword.getText().toString()) || etPassword.getText().toString().equalsIgnoreCase("")) {
            etPassword.setError("Required");
            MessageUtils.showLongToastMsg(getApplicationContext(), "?????? ????????? ????????? ?????????.");
            return;
        }
        etPassword.setError(null);

        if(!isCheckedAccountType()){
            MessageUtils.showLongToastMsg(getApplicationContext(), "Account, Guest ??? ????????? ????????? ?????????..");
            return;
        }


        onFirebaseLogin(etEmail.getText().toString(), etPassword.getText().toString());

    }

    public void onFirebaseLogin(String id, String password) {
        setLoading("????????? ????????????.");
        id+="@naver.com";
        FirebaseAuth.accessToLogin(id, password, new OnResultListener() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task != null) {
                    L.i(":::FirebaseApi.getCurrentUser().getUid() : " + FirebaseAuth.getCurrentUser().getUid());
                    FirebaseDatabase.getInstance().getReference().child(FirebaseDB.HY_ACCOUNTS).child(FirebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(mLoginValueListener);
                }

            }

            @Override
            public void onDelete(Task<Void> task) {

            }

            @Override
            public void onFailure(Exception e) {
                L.e("::::: e : " + e);
//                    ToastUtils.show(,e.getMessage());
                String message = null;
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    message = "??????????????? ???????????? ????????????.";
                } else if (e instanceof FirebaseAuthInvalidUserException) {
                    message = "???????????? ???????????? ????????????.";
                } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    message = "??????????????? ????????? ???????????????.";
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    message = "?????? ?????? ??????????????? ??????????????? ????????????..";
                }
                hideProgressDialog();
                final String finalMessage = message;
                MessageUtils.showLongToastMsg(getApplicationContext(), finalMessage);
            }
        });

    }
}