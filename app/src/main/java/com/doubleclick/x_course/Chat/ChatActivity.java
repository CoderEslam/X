package com.doubleclick.x_course.Chat;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

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
import com.doubleclick.x_course.Premissions.Permissions;
import com.doubleclick.x_course.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatActivity extends AppCompatActivity {

    private CircleImageView profile_image;
    private FirebaseUser fuser;
    private DatabaseReference reference;
    private EditText et_text_send;
    private MessageAdapter messageAdapter;
    private List<Chat> mchat;
    private RecyclerView recyclerView;
    private Intent intent;
    private ValueEventListener seenListener;
    private String userid, audioPath;
    private APIService apiService;
    private LottieAnimationView animationWhatApp, animationCalling;
    private String numberWhats = null;
    boolean notify = false;
    //    private AudioRecorder audioRecorder;
//    private File recordFile;
    private ImageView btnDataSend;
    private final int REQUEST_Code = 20;
    StorageReference storageReference;
    private Uri imageUri;
    private StorageTask uploadTask;
    private ConstraintLayout layout_text;
    private MediaRecorder mediaRecorder;
    private TextView nameFriend;
    private Permissions permissions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
//        audioRecorder = new AudioRecorder();
        intent = getIntent();
        profile_image = findViewById(R.id.profile_image_Chat);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        animationWhatApp = findViewById(R.id.animationWhatApp);
        animationCalling = findViewById(R.id.animationCalling);
        recyclerView = findViewById(R.id.ChatrecyclerView);
        nameFriend = findViewById(R.id.nameFriend);
        recyclerView.setHasFixedSize(true);
        btnDataSend = findViewById(R.id.btnDataSend);
        et_text_send = findViewById(R.id.text_send);
        layout_text = findViewById(R.id.layout_text);
        storageReference = FirebaseStorage.getInstance().getReference("ImagesChat");
        RecordButton sendRecord = findViewById(R.id.sendRecord);
        ImageView sendText = findViewById(R.id.sendText);
        RecordView recordView = findViewById(R.id.recordView);
        sendRecord.setRecordView(recordView);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        userid = intent.getStringExtra("userid");
        numberWhats = intent.getStringExtra("whtatsapp");
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.keepSynced(true);
        sendRecord.setListenForRecord(true);
        nameFriend.setText(getIntent().getStringExtra("nameFriend").toString());
        permissions = new Permissions();
        permissions.isStorageOk(ChatActivity.this);
        permissions.isRecordingOk(ChatActivity.this);
        permissions.requestStorage(ChatActivity.this);
        permissions.requestRecording(ChatActivity.this);
        if (getIntent().getStringExtra("iamgeFriend").equals("default")) {
            profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            //and this
            Glide.with(getApplicationContext()).load(getIntent().getStringExtra("iamgeFriend")).into(profile_image);
        }
        //ListenForRecord must be false ,otherwise onClick will not be called
        sendRecord.setOnRecordClickListener(new OnRecordClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ChatActivity.this, "RECORD BUTTON CLICKED", Toast.LENGTH_SHORT).show();
                Log.d("RecordButton", "RECORD BUTTON CLICKED");
            }
        });
        et_text_send.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                sendRecord.setListenForRecord(true);
                layout_text.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    sendRecord.setVisibility(View.VISIBLE);
                    sendText.setVisibility(View.GONE);
                    recordView.setVisibility(View.VISIBLE);
                    sendRecord.setListenForRecord(true);
                    layout_text.setVisibility(View.GONE);
                } else {
                    layout_text.setVisibility(View.VISIBLE);
                    sendRecord.setListenForRecord(true);
                    //IMPORTANT
                    sendRecord.setVisibility(View.GONE);
                    sendText.setVisibility(View.VISIBLE);
                    recordView.setVisibility(View.GONE);
                    if (sendRecord.isListenForRecord()) {
                        sendRecord.setListenForRecord(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                sendRecord.setListenForRecord(true);
                layout_text.setVisibility(View.VISIBLE);
            }
        });


        //Cancel Bounds is when the Slide To Cancel text gets before the timer . default is 8
        recordView.setCancelBounds(8);
        recordView.setSmallMicColor(Color.parseColor("#FF4081"));
        //prevent recording under one Second
        recordView.setLessThanSecondAllowed(false);
        recordView.setSlideToCancelText("Slide To Cancel");
        recordView.setCustomSounds(R.raw.record_start, R.raw.record_finished, 0);
        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                recordView.setVisibility(View.VISIBLE);
                layout_text.setVisibility(View.INVISIBLE);
                setUpRecording();
                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendText.setVisibility(View.GONE);
//                try {
//                    recordFile = new File(getFilesDir(), UUID.randomUUID().toString() + ".3gp");
//                    audioRecorder.start(recordFile.getPath());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                Log.d("RecordView", "onStart");
                Toast.makeText(ChatActivity.this, "OnStartRecord", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                mediaRecorder.reset();
                mediaRecorder.release();
                File file = new File(audioPath);
                if (file.exists()) {
                    file.delete();
                }
                layout_text.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish(long recordTime) {
                layout_text.setVisibility(View.VISIBLE);
                try {
                    mediaRecorder.stop();
                    mediaRecorder.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sendText.setVisibility(View.VISIBLE);
                sendRecodingMessage(audioPath);
//                String time = getHumanTimeText(recordTime);
//                Toast.makeText(ChatActivity.this, "onFinishRecord - Recorded Time is: " + time + " File saved at " + recordFile.getPath(), Toast.LENGTH_SHORT).show();
//                Log.d("RecordView", "onFinish" + " Limit Reached? ");
//                Log.d("RecordTime", time);
//                sendFile(fuser.getUid(), userid, recordFile.getPath());
            }

            @Override
            public void onLessThanSecond() {
                mediaRecorder.reset();
                mediaRecorder.release();
                File file = new File(audioPath);
                if (file.exists()) {
                    file.delete();
                }
                layout_text.setVisibility(View.VISIBLE);
                sendText.setVisibility(View.VISIBLE);
//                Toast.makeText(ChatActivity.this, "OnLessThanSecond", Toast.LENGTH_SHORT).show();
//                Log.d("RecordView", "onLessThanSecond");
            }
        });
        recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
            @Override
            public void onAnimationEnd() {
                Toast.makeText(ChatActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                Log.d("RecordView", "Basket Animation Finished");
            }
        });

        sendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String msg = et_text_send.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(fuser.getUid(), userid, msg);
                    et_text_send.setText("");
                } else {
                    Toast.makeText(ChatActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        btnDataSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFiles();
            }
        });
    }

    private void openFiles() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_Code);

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
        String pushId = reference.push().getKey().toString();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("type", "text");
        hashMap.put("isseen", false);
        hashMap.put("imageId", pushId);
        reference.child("Chats").child(pushId).setValue(hashMap);
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

    private void readMesagges(final String myid, final String userid, final String imageurl) {
        mchat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.hasChild("type")) {
                        Chat chat = snapshot.getValue(Chat.class);
                        if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                            mchat.add(chat);
                        }

                        messageAdapter = new MessageAdapter(ChatActivity.this, mchat, imageurl);
                        recyclerView.setAdapter(messageAdapter);
                    } else {
                        Chat chat = snapshot.getValue(Chat.class);
                        chat.setType(null);
                        if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                            mchat.add(chat);
                        }

                        messageAdapter = new MessageAdapter(ChatActivity.this, mchat, imageurl);
                        recyclerView.setAdapter(messageAdapter);
                    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            sendImage(fuser.getUid(), userid);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void sendImage(String sender, String FriendId) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading");
        progressDialog.show();
        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        String pushId = reference.push().getKey().toString();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("sender", sender);
                        hashMap.put("receiver", FriendId);
                        hashMap.put("message", mUri);
                        hashMap.put("type", "image");
                        hashMap.put("isseen", false);
                        hashMap.put("imageId", pushId);
                        reference.child("Chats").child(pushId).setValue(hashMap);
                        sendNotifiaction(FriendId, "you have", "Image");


                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(ChatActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(ChatActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpRecording() {

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        File file = new File(Environment.getExternalStorageDirectory(), "Xteam");       // String f = "/storage/emulated/0/Download";
        if (!file.exists()) {
            file.mkdirs();
        }
        audioPath = getFilePath();//file.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".3gp";
        mediaRecorder.setOutputFile(audioPath);
    }

    public void userInfo() {
//        Intent intent = new Intent(this, UserInfo.class);
//        intent.putExtra("userID", userid);
//        startActivity(intent);
    }

    private void sendRecodingMessage(String audioPath) {
        if (audioPath != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("/Media/Recording/" + fuser.getUid() + ":" + userid + System.currentTimeMillis());
            Log.e("audio path", audioPath);
            Uri audioFile = Uri.fromFile(new File(audioPath));
            Log.e("audioFile = ", audioFile.toString());
            storageReference.putFile(audioFile).addOnSuccessListener(success -> {
                Task<Uri> audioUrl = success.getStorage().getDownloadUrl();
                audioUrl.addOnCompleteListener(path -> {
                    if (path.isSuccessful()) {
                        String url = path.getResult().toString();
                        if (userid == null) {
                            Toast.makeText(ChatActivity.this, "No thing to send", Toast.LENGTH_SHORT).show();
                        } else {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
                            Map<String, Object> map = new HashMap<>();
                            String pushId = databaseReference.push().getKey().toString();
                            map.put("sender", fuser.getUid());
                            map.put("receiver", userid);
                            map.put("message", url);
                            map.put("type", "voice");
                            map.put("isseen", false);
                            map.put("imageId", pushId);
                            sendNotifiaction(userid, "you have", "voice");
                            databaseReference.child(pushId).setValue(map);
                        }
                    }
                });
            });
        }
    }

    private String getFilePath() {
        ContextWrapper contextWrapper = new ContextWrapper(this);
        File file = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File f = new File(file, "Xteam.mp3");
        return f.getPath();
    }

}