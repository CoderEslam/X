package com.doubleclick.x_course.NewNotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;
import androidx.navigation.NavDeepLinkBuilder;

import com.doubleclick.x_course.Chat.ChatFragment;
import com.doubleclick.x_course.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessaging extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String sented = remoteMessage.getData().get("sent");
        String user = remoteMessage.getData().get("user");


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            assert sented != null;
            if (sented.equals(firebaseUser.getUid())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    sendOreoNotification(remoteMessage);
                } else {
                    sendNotification(remoteMessage);
                }

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void sendOreoNotification(RemoteMessage remoteMessage){
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = 0;
        try {
            j = Integer.parseInt(user.replaceAll("[\\D]", ""));
        }catch (NumberFormatException e){
            Log.e("FirebaseMessaging",""+user.toString());
//            Toast.makeText(getApplicationContext(), "Firebase Messaging, Error At  67 "+e.toString(), Toast.LENGTH_SHORT).show();
        }

        //Intent intent = new Intent(this, Chatfragment.class);
        Bundle bundle = new Bundle();
        bundle.putString("friendid", user);

        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        SharedPreferences.Editor predsefits = sharedPreferences.edit();
        predsefits.putString("currentuser", user);
        predsefits.apply();


        //intent.putExtras(bundle);
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //  PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);





        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreaNotification oreoNotification = new OreaNotification(this);
        // Notification.Builder builder = oreoNotification.getNotification(title, body, pendingIntent,
        //   defaultSound, icon);


        // todo style
        // todo Remote input message
        RemoteInput remoteInput = new RemoteInput.Builder("key_text_reply").setLabel("Your Message...").build();
        Intent replyIntent;
        PendingIntent pIntentreply = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // N -> Noga

            replyIntent = new Intent(this, NotificationService.class);
            pIntentreply = PendingIntent.getBroadcast(this, 0, replyIntent, 0);


        }

        // todo action
        //todo replay on a message from out.
        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(R.drawable.reply,
                "Reply", pIntentreply).addRemoteInput(remoteInput).build();


        // todo style



        PendingIntent pendgShit = new NavDeepLinkBuilder
                (getApplicationContext()).setGraph(R.navigation.navgraph)
                .setArguments(bundle).setDestination(R.id.chatFragment)
                .createPendingIntent();



        NotificationCompat.Builder builder = oreoNotification.getNotificationShit( replyAction, title, body, pendgShit,
                defaultSound, icon);


        int i = 0;
        if (j > 0){
            i = j;
        }



        SharedPreferences shf = getSharedPreferences("NEWPREFS", MODE_PRIVATE);
        SharedPreferences.Editor editorSh = shf.edit();
        editorSh.putInt("values", i);
        editorSh.apply();


        oreoNotification.getManager().notify(i, builder.build());

    }

    public void sendNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, ChatFragment.class);
        Bundle bundle = new Bundle();
        bundle.putString("friendid", user);


        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);
        NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        int i = 0;
        if (j > 0){
            i = j;
        }

        noti.notify(i, builder.build());
    }
}