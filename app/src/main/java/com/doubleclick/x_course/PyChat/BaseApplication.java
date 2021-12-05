package com.doubleclick.x_course.PyChat;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.google.firebase.database.FirebaseDatabase;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

public class BaseApplication extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        ConnectivityReceiver.init(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        EmojiManager.install(new GoogleEmojiProvider());
    }
}
