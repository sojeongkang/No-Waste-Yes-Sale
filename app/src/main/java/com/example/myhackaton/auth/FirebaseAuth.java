package com.example.myhackaton.auth;


import com.example.myhackaton.L;
import com.google.firebase.auth.FirebaseUser;


public class FirebaseAuth {

    public static void logout() {
        //Firebase 로그아웃 로직.
        com.google.firebase.auth.FirebaseAuth.getInstance().signOut();
    }

    public static void delete(final OnResultListener listener) {
        if (com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                L.i("Firebase User Delete");
                listener.onDelete(task);
            } else {
                L.i("Firebase User Fail");
                listener.onDelete(null);
            }
        });


    }

    public static FirebaseUser getCurrentUser() {
        if (com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser() == null) {
            return null;
        }
        return com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
    }



    public static void accessToLogin(String id, String password, final OnResultListener listener) {
        com.google.firebase.auth.FirebaseAuth auth = com.google.firebase.auth.FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(id, password).addOnCompleteListener(task -> {
            L.e(":::::: [Sing In Complete] : " + task.isSuccessful());
            if (task.isSuccessful()) {
                listener.onComplete(task);
            } else {
                L.e(":::::::::::::: Sing In Failed");
                listener.onComplete(null);
            }
        }).addOnFailureListener(e -> listener.onFailure(e));
    }
}
