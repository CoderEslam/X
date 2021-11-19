package com.doubleclick.x_course.Chat;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordClickListener;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
import com.doubleclick.x_course.API.APIService;
import com.doubleclick.x_course.Adapter.MessageAdapter;
import com.doubleclick.x_course.Model.Chat;
import com.doubleclick.x_course.Model.User;
import com.doubleclick.x_course.Notifications.Client;
import com.doubleclick.x_course.Notifications.Data;
import com.doubleclick.x_course.Notifications.MyResponse;
import com.doubleclick.x_course.Notifications.Sender;
import com.doubleclick.x_course.Notifications.Token;
import com.doubleclick.x_course.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatActivity extends AppCompatActivity {

    private CircleImageView profile_image;
    private FirebaseUser fuser;
    private DatabaseReference reference;
    private EditText text_send;
    private MessageAdapter messageAdapter;
    private List<Chat> mchat;
    private RecyclerView recyclerView;
    private Intent intent;
    private ValueEventListener seenListener;
    private String userid;
    private APIService apiService;
    private LottieAnimationView animationWhatApp, animationCalling;
    private String numberWhats = null;
    boolean notify = false;
    private AudioRecorder audioRecorder;
    private File recordFile;
    RecordView recordView;
    RecordButton btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        animationWhatApp = findViewById(R.id.animationWhatApp);
        animationCalling = findViewById(R.id.animationCalling);
        recyclerView = findViewById(R.id.ChatrecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        profile_image = findViewById(R.id.profile_image_Chat);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        intent = getIntent();
        userid = intent.getStringExtra("userid");
        numberWhats = intent.getStringExtra("whtatsapp");
        recordView = findViewById(R.id.record_view);
        btn_send.setRecordView(recordView);
        btn_send.setOnRecordClickListener(new OnRecordClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String msg = text_send.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(fuser.getUid(), userid, msg);
                } else {
                    Toast.makeText(ChatActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });
        //Cancel Bounds is when the Slide To Cancel text gets before the timer . default is 8
        recordView.setCancelBounds(8);
        recordView.setSmallMicColor(Color.parseColor("#c2185b"));
        //prevent recording under one Second
        recordView.setLessThanSecondAllowed(false);
        recordView.setSlideToCancelText("Slide To Cancel");
        recordView.setCustomSounds(R.raw.record_start,R.raw.record_finished, 0);
        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                recordFile = new File(getFilesDir(), UUID.randomUUID().toString() + ".mp3");//getFilesDir(), UUID.randomUUID().toString() + ".3gp");
                try {
                    audioRecorder.start(recordFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("RecordView", "onStart");

                Toast.makeText(ChatActivity.this, "OnStartRecord", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                stopRecording(true);

                Toast.makeText(ChatActivity.this, "onCancel", Toast.LENGTH_SHORT).show();

                Log.d("RecordView", "onCancel");
            }

            @Override
            public void onFinish(long recordTime) {
                stopRecording(false);
                String time = getHumanTimeText(recordTime);
                Toast.makeText(ChatActivity.this, "onFinishRecord - Recorded Time is: " + time + " File saved at " + recordFile.getPath(), Toast.LENGTH_SHORT).show();
                Log.d("RecordView", "onFinish" + " Limit Reached? ");//+ limitReached);
                Log.d("RecordTime", time);
                functionTeilen(recordFile.getPath());
            }

            @Override
            public void onLessThanSecond() {
                stopRecording(true);

                Toast.makeText(ChatActivity.this, "OnLessThanSecond", Toast.LENGTH_SHORT).show();
                Log.d("RecordView", "onLessThanSecond");
            }
        });

        recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
            @Override
            public void onAnimationEnd() {
                Log.d("RecordView", "Basket Animation Finished");
            }
        });


        fuser = FirebaseAuth.getInstance().getCurrentUser();
        animationWhatApp.setOnClickListener(view -> {
            if (numberWhats != null) {
                try {
                    Uri whatAppUri = Uri.parse("smsto:" + numberWhats);
                    Intent intentWhats = new Intent(Intent.ACTION_SENDTO, whatAppUri);
                    intentWhats.setPackage("com.whatsapp");
                    startActivity(intentWhats);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(ChatActivity.this, "You don't have whatsapp!", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "your friend doen't has a whats app", Toast.LENGTH_SHORT).show();
            }
        });
        animationCalling.setOnClickListener(view -> {
            if (numberWhats != null) {
                try {
                    Uri callUri = Uri.parse("tel:" + numberWhats);
                    Intent intentCall = new Intent(Intent.ACTION_DIAL, callUri);
                    startActivity(intentCall);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(ChatActivity.this, "You don't have call app!", Toast.LENGTH_LONG).show();

                }

            } else {
                Toast.makeText(getApplicationContext(), "your friend doen't has a  Number", Toast.LENGTH_SHORT).show();
            }
        });


        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getImageURL().equals("default")) {
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {
                    //and this
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                }

                readMesagges(fuser.getUid(), userid, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        seenMessage(userid);
        PermissionHandler();

    }

    private boolean PermissionHandler() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        boolean recordPermissionAvailable = ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.RECORD_AUDIO) == PERMISSION_GRANTED;
        if (recordPermissionAvailable) {
            return true;
        }

        ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);

        return false;
    }

    public void functionTeilen(String file) { // this fun response to send
        Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/raw/" + file);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("audio/mpeg3");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Audio teilen"));
    }


    private void stopRecording(boolean deleteFile) {
        audioRecorder.stop();
        if (recordFile != null && deleteFile) {
            recordFile.delete();
        }
    }

    private void seenMessage(final String userid) {
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(userid)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, final String receiver, String message) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);

        reference.child("Chats").push().setValue(hashMap);


        // add user to chat fragment
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(fuser.getUid())
                .child(userid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(userid)
                .child(fuser.getUid());
        chatRefReceiver.child("id").setValue(fuser.getUid());

        final String msg = message;

        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (notify) {
                    sendNotifiaction(receiver, user.getUsername(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String getHumanTimeText(long milliseconds) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(milliseconds), TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    private void sendNotifiaction(String receiver, final String username, final String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
//                    Toast.makeText(ChatActivity.this,""+token.toString(),Toast.LENGTH_SHORT).show();
                    // icon notification show up
                    Data data = new Data(fuser.getUid(), R.drawable.ic_baseline_send_24, username + " : " + message, "New Message", userid);
//                    Toast.makeText(ChatActivity.this,""+data.toString(),Toast.LENGTH_SHORT).show();

                    Sender sender = new Sender(data, token.getToken());
//                    Toast.makeText(ChatActivity.this,""+sender.toString(),Toast.LENGTH_SHORT).show();

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
//                                    Toast.makeText(ChatActivity.this,""+response.code(),Toast.LENGTH_SHORT).show();
                                    if (response.code() == 200) {
                                        if (response.body().success != 1) {  // must be equal 1
                                            Toast.makeText(ChatActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void SendNotification(String friendid, String nameofsender, String message) {

        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(friendid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapsho : snapshot.getChildren()) {
                    Token token1 = snapsho.getValue(Token.class);
                    Data data = new Data(fuser.getUid(), R.drawable.ic_baseline_send_24, message, "New Message From " + nameofsender, friendid);
                    Sender sender = new Sender(data, token1.getToken());
                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() == 200) {
                                if (response.body().success != 1) {  // must be equal 1
                                    Toast.makeText(ChatActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMesagges(final String myid, final String userid, final String imageurl) {
        mchat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                        mchat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(ChatActivity.this, mchat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void currentUser(String userid) {
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();
    }

    private void status(String status) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
        currentUser(userid);
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");
        currentUser("none");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}