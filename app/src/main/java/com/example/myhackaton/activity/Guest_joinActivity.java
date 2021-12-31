package com.example.myhackaton.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myhackaton.FirebaseDB;
import com.example.myhackaton.L;
import com.example.myhackaton.R;
import com.example.myhackaton.RootActivity;
import com.example.myhackaton.helper.MessageUtils;
import com.example.myhackaton.model.Account;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import butterknife.OnClick;

public class Guest_joinActivity extends RootActivity {
    public final static int REQUEST_CODE = 0x02;
    private static final String TAG = Guest_joinActivity.class.getSimpleName();
    //public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private Button bt_guest_join;
    private EditText inputguestid;
    private EditText inputguestpw;
    private EditText inputguest_phone;
    private CheckBox cbLocation;
    private CheckBox cbAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_guest_join);
        super.onCreate(savedInstanceState);

        inputguestid = findViewById(R.id.guestid);
        inputguestpw = findViewById(R.id.guestpw);
        inputguest_phone = findViewById(R.id.guest_phone);
        cbLocation = findViewById(R.id.cb_location);
        cbAlarm = findViewById(R.id.cb_alarm);
        bt_guest_join = findViewById(R.id.bt_guest_join);
    }


    @OnClick(R.id.bt_guest_join)
    public void onClick(View view) {
//        Intent intent = new Intent(getApplicationContext(), Login_mainActivity.class);
//        startActivity(intent);

        String guestid = inputguestid.getText().toString().trim();
        String guestpw = inputguestpw.getText().toString().trim();
        String guest_phone = inputguest_phone.getText().toString().trim();

        if(!cbLocation.isChecked() || !cbAlarm.isChecked()){
            Toast.makeText(getApplicationContext(), "필수 항목을 체크해 주세요.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!guestid.isEmpty() && !guestpw.isEmpty() && !guest_phone.isEmpty()) {
            registerUser(guestid, guestpw, guest_phone);
        } else {
            Toast.makeText(getApplicationContext(), "필수사항은 모두 입력하세요!", Toast.LENGTH_LONG).show();
        }
    }


    private void registerUser(String id, String pwd, String phone) {
        id += "@naver.com";

        Account guest = Account.builder().email(id).phoneNumber(phone).accountType(FirebaseDB.HY_USER_GUEST)
                .storeName("").storeLocation("").detailAddress("").documentPhoto("").menuList("").build();
        checkVerfication(guest, pwd);

//        GetDataJSON g = new GetDataJSON();
//        g.execute("http://ansdmsry04.dothome.co.kr" + "/guestTable.php", id, password, mobileNO);
//        String s = g.toString();
//        Log.d(TAG, s);
    }

    public void checkVerfication(Account guest, String password) {
        L.e("guest = "+ guest);
        if (guest != null) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(guest.getEmail(), password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    registerSuccess(task.getResult().getUser(), guest);
                } else {
                    MessageUtils.showLongToastMsg(getApplicationContext(), "알수없는 오류로 실패 하였습니다. 다시 시도해주세요.");
                }
            }).addOnFailureListener(e -> MessageUtils.showLongToastMsg(getApplicationContext(), "알수없는 오류로 실패 하였습니다. 다시 시도해주세요."));
        }
    }

    private void registerSuccess(FirebaseUser firebaseUser, Account guest) {
        L.i(":::::[registerSuccess] " + guest.toString());
        setLoading("가입 중입니다.");

        Account temp = guest;
        temp.setEmail(firebaseUser.getEmail());
        temp.setAddedByUser(firebaseUser.getUid());

        L.i("[registerSuccess] : " + temp.toString());
        FirebaseDatabase.getInstance().getReference().child(FirebaseDB.HY_ACCOUNTS).child(firebaseUser.getUid()).setValue(temp).addOnCompleteListener(task1 -> {
            L.i(":::: task.isSuccessful : " + task1.isSuccessful());
            if (task1.isSuccessful()) {
                hideProgressDialog();
                setLoading("환영합니다!\n가입이 완료 되었습니다.");
                mPreferenceHelper.setCurrentAccount(temp);
                new android.os.Handler().postDelayed(() -> {
                    hideProgressDialog();
                    onSuccess();
                }, 1000);

            }
        });
    }

    private void onSuccess() {
//        com.example.myhackaton.auth.FirebaseAuth.logout();
//        onBackPressed();
        Intent intent = new Intent(getApplicationContext(), Guest_mainActivity.class);
        startActivityWithAni(intent);
        setResult(Activity.RESULT_OK);
        onBackPressed();
    }

    class GetDataJSON extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String uri = params[0];
            String id = params[1];
            String pw = params[2];
            String no = params[3];
            String postParameters = "guestid=" + id + "&guestpw=" + pw + "&guest_phone=" + no;

            try {
                URL url = new URL(uri);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();


            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return "Error: " + e.getMessage();
            }
        }
    }
}







