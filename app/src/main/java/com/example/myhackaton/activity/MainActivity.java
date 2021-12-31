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
                    MessageUtils.showLongToastMsg(getApplicationContext(), "로그인에 성공하였습니다.");

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
                            Toast.makeText(getApplicationContext(), "Account, Guest 먼저 선택하세요", Toast.LENGTH_SHORT)
                                    .show();
                    }
                }
            } else {
                //로그인 실패시 로직처리
                hideProgressDialog();
                MessageUtils.showLongToastMsg(getApplicationContext(), "로그인에 실패 하였습니다..");
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
                L.e("메인 화면 종료");
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
                Toast.makeText(getApplicationContext(), "Account, Guest 먼저 선택하세요", Toast.LENGTH_SHORT)
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
        //키보드 자판기를 닫아준다.
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
        //로그인을 시도한다.
        //키보드 화면을 닫아준다.
        hideKeyboard();
        //아이디가 공백이라면 Toast 메세지를 띄워주고 진행시키지않는다.
        if (TextUtils.isEmpty(etEmail.getText().toString()) || etEmail.getText().toString().equalsIgnoreCase("")) {
            etEmail.setError("Required");
            MessageUtils.showLongToastMsg(getApplicationContext(), "ID를 입력해 주세요");
            return;
        }
        etEmail.setError(null);

        //패스워드 공백이라면 Toast 메세지를 띄워주고 진행시키지않는다.
        if (TextUtils.isEmpty(etPassword.getText().toString()) || etPassword.getText().toString().equalsIgnoreCase("")) {
            etPassword.setError("Required");
            MessageUtils.showLongToastMsg(getApplicationContext(), "비밀 번호를 입력해 주세요.");
            return;
        }
        etPassword.setError(null);

        if(!isCheckedAccountType()){
            MessageUtils.showLongToastMsg(getApplicationContext(), "Account, Guest 중 하나를 선택해 주세요..");
            return;
        }


        onFirebaseLogin(etEmail.getText().toString(), etPassword.getText().toString());

    }

    public void onFirebaseLogin(String id, String password) {
        setLoading("로그인 중입니다.");
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
                    message = "비밀번호가 일치하지 않습니다.";
                } else if (e instanceof FirebaseAuthInvalidUserException) {
                    message = "아이디가 존재하지 않습니다.";
                } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    message = "이메일주소 양식을 지켜주세요.";
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    message = "너무 잦은 시도로인해 로그인할수 없습니다..";
                }
                hideProgressDialog();
                final String finalMessage = message;
                MessageUtils.showLongToastMsg(getApplicationContext(), finalMessage);
            }
        });

    }
}