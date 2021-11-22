package com.doubleclick.x_course.Chat;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseSaveInstance extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
