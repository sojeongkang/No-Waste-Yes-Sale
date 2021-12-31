package com.example.myhackaton.auth;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface OnResultListener {
    void onComplete(Task<AuthResult> task);
    void onDelete(Task<Void> task);
    void onFailure(Exception e);
}
