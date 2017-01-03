package com.example.mdijajsayim.myproject;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Md Ijaj Sayim on 23-Dec-16.
 */

public class MyProject extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
