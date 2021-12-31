/**
 * Copyright © 2016. by LG Electronics Inc.
 * <p>
 * This program or software including the accompanying associated documentation
 * (“Software”) is the proprietary software of LG Electronics Inc. and or its
 * licensors, and may only be used, duplicated, modified or distributed pursuant
 * to the terms and conditions of a separate written license agreement between
 * you and LG Electronics Inc. (“Authorized License”). Except as set forth in
 * an Authorized License, LG Electronics Inc. grants no license (express or
 * implied), rights to use, or waiver of any kind with respect to the Software,
 * and LG Electronics Inc. expressly reserves all rights in and to the Software
 * and all intellectual property therein. If you have no Authorized License,
 * then you have no rights to use the Software in any ways, and should
 * immediately notify LG Electronics Inc. and discontinue all use of the Software.
 */

package com.example.myhackaton.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myhackaton.L;
import com.example.myhackaton.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.List;



/**
 * App에서 쓰이는 사용자 image loading, cropping, caching, permission checks 등의 기능을 구현하기 위한
 * 3rd party libraries의 동작 및 관련 설정들을 포함하는 wrapper class
 */
public class ImageLoaderHelper {

    /**
     * 지정한 File들을 제거하는 AsyncTask.
     */
    private static class DeleteFileAsyncTask extends AsyncTask<File, Void, Void> {
        private static String TAG = DeleteFileAsyncTask.class.getSimpleName();

        private void deleteRecursive(File f) {
            if (f.isDirectory()) {
                File[] files = f.listFiles();
                if (null != files && files.length > 0) {
                    for (File child : files) {
                        L.d("[delete a file] : " + child.toString());
                        deleteRecursive(child);
                    }
                }
            }

            if (true == f.delete()) {
                L.d("[deleteRecursive] delete success.");
            } else {
                //TODO 필요시 false 일 경우 처리 (함수 Retry or AsyncTask Retry) 등
                L.d("[deleteRecursive] delete fail.");
            }
        }

        @Override
        protected Void doInBackground(File... files) {
            for (File f : files) {
                deleteRecursive(f);
            }
            return null;
        }
    }

    public static int OPEN_IMAGE_REQUEST_CODE = 49018;
    private static String CACHE_DIR_NAME = "pictures";

    private static String TAG = ImageLoaderHelper.class.getSimpleName();
    private Context mContext;
    private Activity mActivity;
    private String[] mItems = null;
    private File mCacheDir;
    private Uri mCameraUri;

    public Uri getmCameraUri() {
        return mCameraUri;
    }

    public void setmCameraUri(Uri mCameraUri) {
        this.mCameraUri = mCameraUri;
    }

    /**
     * Injectable Constructor
     */
    public ImageLoaderHelper(Context context) {
        mContext = context;
        mItems = new String[]{
                "사진 촬영",
                "사진 선택"
        };
        createCacheDirectory();
    }

    private boolean createCacheDirectory() {
        mCacheDir = new File(mContext.getCacheDir() + File.separator +
                CACHE_DIR_NAME + File.separator);

        if (true == mCacheDir.exists()) {
            return true;
        }

        if (true == mCacheDir.mkdir()) {
            L.d("[ImageLoaderHelper] mkdir success.");
            return true;
        } else {
            //TODO 필요시 fail flag를 설정해서 실제 LoaderHelper를 사용하기 전에 retry 하도록 함.
            L.d("[ImageLoaderHelper] mkdir fail.");
            return false;
        }
    }

    /**
     * Image를 어디서 불러올 것인지 (카메라 or 사진첩)를 선택하는 dialog의 builder를 만들어 return
     */
    public AlertDialog.Builder imageLoaderDialogBuilder(Activity activity) {
        setmCameraUri(null);
        return new AlertDialog.Builder(mContext)
                .setItems(mItems, (dialogInterface, i) -> {
                    Intent intent;
                    if (i == 0) {
                        getCameraIntent(activity);
                        return;
                    } else {
                        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    }
                    activity.startActivityForResult(intent, OPEN_IMAGE_REQUEST_CODE);
                })
                .setNegativeButton("취소", ((dialogInterface, i) -> {
                }));
    }


    public AlertDialog.Builder imageLoaderDialogBuilderEdit(Activity activity) {
        mItems = new String[]{
                "사진 촬영",
                "사진 선택,"
        };

        setmCameraUri(null);
        return new AlertDialog.Builder(mContext).setItems(mItems, (dialogInterface, i) -> {
            if (i == 0) {
                getCameraIntent(activity);
            } else if (i == 1) {
                getGallaryIntent(activity);

            }
        }).setNegativeButton("취소", ((dialogInterface, i) -> {
        }));
    }


    /**
     * 지정된 imageView에 image를 설정
     */
    public static void setProfileImage(Context context, Uri imageUri, ImageView imageView, String imageUpdate) {
        if (null == imageUri || "".equals(imageUri.toString())) {
            int placeHolderResId = R.mipmap.ic_launcher;
            Glide.with(context)
                    .load(imageUri)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .placeholder(placeHolderResId)
                    .centerCrop()
                    .fitCenter()
                    .dontAnimate()
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(imageUri)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .dontAnimate()
                    .into(imageView);
        }
    }

    public static void setProfileImage(Context context, byte[] imageUri, ImageView imageView, String imageUpdate) {
        if (null == imageUri || imageUri.length == 0) {
            int placeHolderResId = R.mipmap.ic_launcher;
            Glide.with(context)
                    .load(imageUri)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .placeholder(placeHolderResId)
                    .centerCrop()
                    .fitCenter()
                    .dontAnimate()
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(imageUri)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .fitCenter()
                    .dontAnimate()
                    .into(imageView);
        }
    }

    private void getCameraIntent(Activity activity) {

        Dexter.withActivity(activity).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    String folderName = "hackaton";// 폴더명
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName;
                    File fileFolderPath = new File(path);
                    if (!fileFolderPath.exists()) {
                        if (false == fileFolderPath.mkdir()) {
                            L.d("[getCameraIntent] mkdir fail.");
                            //TODO: 필요시 실패 Dialog 표시
                            return;
                        }
                    }

                    String url = "hackaton_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                    Uri outputFileUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", new File(path, url));

                    List<ResolveInfo> resolvedIntentActivities = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                        String packageName = resolvedIntentInfo.activityInfo.packageName;

                        mContext.grantUriPermission(packageName, outputFileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }

                    setmCameraUri(outputFileUri);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    activity.startActivityForResult(intent, OPEN_IMAGE_REQUEST_CODE);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    private void getGallaryIntent(Activity activity) {
        Dexter.withActivity(activity).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    activity.startActivityForResult(intent, OPEN_IMAGE_REQUEST_CODE);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();
    }
}


