package com.example.myhackaton.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myhackaton.FirebaseDB;
import com.example.myhackaton.L;
import com.example.myhackaton.R;
import com.example.myhackaton.RootActivity;
import com.example.myhackaton.helper.ImageLoaderHelper;
import com.example.myhackaton.helper.MessageUtils;
import com.example.myhackaton.model.Account;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import butterknife.OnClick;

public class Host_joinActivity extends RootActivity {
    public final static int REQUEST_CODE = 0x01;
    private static final String TAG = Host_joinActivity.class.getSimpleName();
    //public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public SharedPreferences settings;
    private Button bt_host_join;
    private EditText inputhostid;
    private EditText inputhostpw;
    private EditText inputstore;
    private TextView inputstoreLocation;
    private EditText inputaddressText;
    private EditText menuList;
    private Button map;
    private Uri mDocumentPhoto = null;

    private ImageLoaderHelper mImageLoader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_host_join);
        super.onCreate(savedInstanceState);
        mImageLoader = new ImageLoaderHelper(this);

        inputhostid = findViewById(R.id.hostid);
        inputhostpw = findViewById(R.id.hostpw);
        inputstore = findViewById(R.id.store);
        inputstoreLocation = findViewById(R.id.storeLocation);
        inputaddressText = findViewById(R.id.addressText);
        menuList = findViewById(R.id.menudata);
        map = findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(Host_joinActivity.this, AlarmActivity.class);
                mapIntent.putExtra("isFromHostJoin", true);
                startActivityWithAniForResult(mapIntent, AlarmActivity.REQUEST_CODE);
            }
        });
    }

    @OnClick(R.id.bt_host_join)
    public void onClick(View view) {

//                Intent intent = new Intent(getApplicationContext(), Login_mainActivity.class);
//                startActivity(intent);

        String hostid = inputhostid.getText().toString().trim();
        String hostpw = inputhostpw.getText().toString().trim();
        String store = inputstore.getText().toString().trim();
        String storeLocation = inputstoreLocation.getText().toString().trim();
        String detailAddress = inputaddressText.getText().toString().trim();
        String menu = menuList.getText().toString().trim();


        L.e();
        if (!hostid.isEmpty() && !hostpw.isEmpty() && !store.isEmpty() && !detailAddress.isEmpty() && !storeLocation.isEmpty()&& !menu.isEmpty()) {
            registerUser(hostid, hostpw, store, storeLocation, detailAddress, menu);
        } else {
            Toast.makeText(getApplicationContext(), "필수사항은 모두 입력하세요!", Toast.LENGTH_LONG).show();
        }
    }

    private void registerUser(String id, String pwd, String store, String storeLocation, String address, String menu) {
        id += "@naver.com";

        Account account = Account.builder().email(id).storeName(store).storeLocation(storeLocation).detailAddress(address)
                .menuList(menu).phoneNumber("").accountType(FirebaseDB.HY_USER_HOST).build();
        checkVerfication(account, pwd);
//        Host_joinActivity.GetDataJSON g = new Host_joinActivity.GetDataJSON();
//        g.execute("http://ansdmsry04.dothome.co.kr" + "/account.php", id,password,store,address);
//        String s = g.toString();
//        Log.d(TAG, s);
    }

    public void checkVerfication(Account account, String password) {
        L.e("account = "+ account);
        if (account != null) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(account.getEmail(), password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    registerSuccess(task.getResult().getUser(), account);
                } else {
                    L.e("task.getException().getMessage() = "+ task.getException().getMessage());
                    MessageUtils.showLongToastMsg(getApplicationContext(), "알수없는 오류로 실패 하였습니다. 다시 시도해주세요.");
                }
            }).addOnFailureListener(e -> MessageUtils.showLongToastMsg(getApplicationContext(), "알수없는 오류로 실패 하였습니다. 다시 시도해주세요."));
        }
    }

    private void registerSuccess(FirebaseUser firebaseUser, Account account) {
        L.i(":::::[registerSuccess] " + account.toString());
        setLoading("가입 중입니다.");

//        Account temp = account;
        account.setEmail(firebaseUser.getEmail());
        account.setAddedByUser(firebaseUser.getUid());

        if (mDocumentPhoto == null) {
            account.setDocumentPhoto("");
            L.i("[registerSuccess] : " + account.toString());
            FirebaseDatabase.getInstance().getReference().child(FirebaseDB.HY_ACCOUNTS).child(firebaseUser.getUid()).setValue(account).addOnCompleteListener(task1 -> {
                L.i(":::: task.isSuccessful : " + task1.isSuccessful());
                if (task1.isSuccessful()) {
                    mPreferenceHelper.setCurrentAccount(account);
                    hideProgressDialog();
                    setLoading("환영합니다!\n가입이 완료 되었습니다.");
                    new android.os.Handler().postDelayed(() -> {
                        hideProgressDialog();
                        onSuccess();
                    }, 1000);

                }
            });
        } else {
            String storageKey = FirebaseDatabase.getInstance().getReference().child(FirebaseDB.HY_ACCOUNTS).push().getKey();
            FirebaseStorage fs = FirebaseStorage.getInstance();
            StorageReference imagesRef = fs.getReference().child(FirebaseDB.HY_ACCOUNTS).child(firebaseUser.getUid()).child(storageKey);
            account.setDocumentPhoto(storageKey);
            imagesRef.putFile(mDocumentPhoto).addOnSuccessListener(taskSnapshot -> {
                DatabaseReference req = FirebaseDatabase.getInstance().getReference().child(FirebaseDB.HY_ACCOUNTS).child(firebaseUser.getUid());
                req.setValue(account).addOnCompleteListener(task -> {
                    if (!isFinishing()) {
                        hideProgressDialog();
                        new android.os.Handler().postDelayed(() -> {
                            setLoading("환영합니다!\n가입이 완료 되었습니다..");
                            onSuccess();
                        }, 1000);
                    }

                });
            }).addOnFailureListener(e -> hideProgressDialog()).addOnProgressListener(taskSnapshot -> {
                int progress = (int) ((100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                L.i("::::::업로즈 진행상황...  " + progress);
            });

        }


    }

    private void onSuccess() {
//        com.example.myhackaton.auth.FirebaseAuth.logout();
//        onBackPressed();
        Intent intent = new Intent(getApplicationContext(), Host_mainActivity.class);
        startActivityWithAni(intent);
        setResult(Activity.RESULT_OK);
        onBackPressed();
    }

    @OnClick(R.id.attached_file)
    public void onSelectImages(View view) {
        mImageLoader.imageLoaderDialogBuilder(Host_joinActivity.this).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageLoaderHelper.OPEN_IMAGE_REQUEST_CODE) {
            Uri mUri = null;
            if (mImageLoader.getmCameraUri() != null) {
                mUri = mImageLoader.getmCameraUri();
            } else {
                if (data == null) {
                    return;
                }
                mUri = data.getData();
            }
            mDocumentPhoto = mUri;
            L.e("menuList.toString().trim() = "+ menuList.toString().trim());
            if(menuList.toString().trim().isEmpty()){
                menuList.append(mUri.toString());
            }else{
                menuList.append(",");
                menuList.append(mUri.toString());
            }
        }else if(requestCode == AlarmActivity.REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                LatLng location = data.getParcelableExtra("location");
                L.e("location = "+ location);
                if(location != null){
                    StringBuilder sb = new StringBuilder();
                    sb.append(location.latitude);
                    sb.append(",");
                    sb.append(location.longitude);
                    inputstoreLocation.setText(sb.toString());
                }
            }
        }
    }
    //    private void setImageLoad(Uri result) {
//        Glide.with(this)
//                .load(result)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .centerCrop()
//                .dontAnimate()
//                .into(ivLecturePhoto);
//    }
    class GetDataJSON extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String uri = params[0];
            String id = params[1];
            String pw = params[2];
            String store = params[3];
            String address = params[4];
            String postParameters = "hostid=" + id + "&hostpw=" + pw + "&store=" + store + "&addressText" + address;

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




