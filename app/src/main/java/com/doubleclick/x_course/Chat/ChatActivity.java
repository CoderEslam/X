package com.doubleclick.x_course.Chat;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import com.doubleclick.x_course.AudioRecordWhatsapp.AttachmentOption;
import com.doubleclick.x_course.AudioRecordWhatsapp.AttachmentOptionsListener;
import com.doubleclick.x_course.Model.Chat;
import com.doubleclick.x_course.Notifications.Client;
import com.doubleclick.x_course.Notifications.Data;
import com.doubleclick.x_course.Notifications.MyResponse;
import com.doubleclick.x_course.Notifications.Sender;
import com.doubleclick.x_course.Notifications.Token;
import com.doubleclick.x_course.Premissions.Permissions;
import com.doubleclick.x_course.PyChat.models.User;
import com.doubleclick.x_course.R;
import com.doubleclick.x_course.RoomDataBase.AddChatViewModel;
import com.doubleclick.x_course.UserInfo.UserInfoActivity;
import com.doubleclick.x_course.ViewModel.ChatsViewModel;
import com.doubleclick.x_course.ViewModel.UserViewModel;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.vanniktech.emoji.EmojiPopup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatActivity extends AppCompatActivity implements AttachmentOptionsListener {

    private CircleImageView profile_image;
    private FirebaseUser fuser;
    private DatabaseReference reference;
    private EditText et_text_send;
    //    private MessageAdapter messageAdapter;
    private List<Chat> mchat;
    private RecyclerView recyclerView;
    private ValueEventListener seenListener;
    private String userid, audioPath, MyName;
    private APIService apiService;
    private LottieAnimationView animationWhatApp, animationCalling;
    private String numberWhats = null;
    boolean notify = false;
    private ImageView emotion;
    private final int REQUEST_Code = 20;
    private StorageReference storageReference;
    private Uri imageUri;
    private StorageTask uploadTask;
    private ConstraintLayout layout_text;
    private MediaRecorder mediaRecorder;
    private TextView nameFriend;
    private Permissions permissions;
    private AddChatViewModel addChatViewModel;
    private RecordButton sendRecord;
    private ImageView sendText, attach_file;
    private RecordView recordView;
    private boolean cklicked = true;
    private ChatsViewModel chatsViewModel;
    private FirebaseFirestore firestore;
    private UserViewModel userViewModel, MyNameViewModel;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    private LinearLayout info;
    private User mUser;
    private ListenerRegistration listenerRegistration;
    private List<Chat> DatabaseList = new ArrayList<>();
    LifecycleOwner lifecycleOwner;
    private MessageAdapter messageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        lifecycleOwner = this;
        try {
            userid = getIntent().getStringExtra("userid");
        } catch (NullPointerException e) {
        }
        addChatViewModel = new ViewModelProvider(this).get(AddChatViewModel.class); //Room Database
        addChatViewModel.getAllChats(FirebaseAuth.getInstance().getCurrentUser().getUid().toString(), userid);
        chatsViewModel = new ViewModelProvider(this).get(ChatsViewModel.class);//FireStore Database
        try {
            userid = getIntent().getStringExtra("userid");
        } catch (NullPointerException e) {
        }
//        userViewModel = new UserViewModel(getIntent().getStringExtra("userid"));
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        MyNameViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        MyNameViewModel.LoadById(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        MyNameViewModel.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                MyName = user.getUsername();
            }
        });
        userViewModel.LoadById(userid);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        info = findViewById(R.id.info);
        userViewModel.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                mUser = user;
            }
        });
        firestore = FirebaseFirestore.getInstance();
        profile_image = findViewById(R.id.profile_image_Chat);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        animationWhatApp = findViewById(R.id.animationWhatApp);
        animationCalling = findViewById(R.id.animationCalling);
        recyclerView = findViewById(R.id.ChatrecyclerView);
        nameFriend = findViewById(R.id.nameFriend);
        recyclerView.setHasFixedSize(true);
        emotion = findViewById(R.id.emotion);
        et_text_send = findViewById(R.id.text_send);
        layout_text = findViewById(R.id.layout_text);
        attach_file = findViewById(R.id.attach_file);
        storageReference = FirebaseStorage.getInstance().getReference("ImagesChat");
        sendRecord = findViewById(R.id.sendRecord);
        sendText = findViewById(R.id.sendText);
        recordView = findViewById(R.id.recordView);
        sendRecord.setRecordView(recordView);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        numberWhats = getIntent().getStringExtra("whtatsapp");
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.keepSynced(true);
        sendRecord.setListenForRecord(true);
        nameFriend.setText(getIntent().getStringExtra("nameFriend").toString());
        permissions = new Permissions();
        permissions.isStorageOk(ChatActivity.this);
        permissions.isRecordingOk(ChatActivity.this);
        permissions.requestStorage(ChatActivity.this);
        permissions.requestRecording(ChatActivity.this);
        EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(findViewById(R.id.root_view)).build(et_text_send);
        emotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cklicked) {
                    emojiPopup.toggle();
                    cklicked = false;
                    emotion.setImageDrawable(getResources().getDrawable(R.drawable.keyboard));
                } else {
                    emojiPopup.dismiss();
                    cklicked = true;
                    emotion.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_insert_emoticon_24));
                }
            }
        });
        // to delete all data in my phone .
//        addChatViewModel.deleteAllData();
        if (networkInfo != null && networkInfo.isConnected()) {// from firestore
            chatsViewModel.getMessageFromFireSTORE(userid);
            chatsViewModel.ReturnMyMessages().observe(this, new Observer<List<Chat>>() {
                @Override
                public void onChanged(List<Chat> FireList) {
                    /*addChatViewModel.getAllChatDatabase().observe(lifecycleOwner, new Observer<List<Chat>>() { // from room data base
                        @Override
                        public void onChanged(List<Chat> chats) {
                            try {
                                for (int i = 0; i < FireList.size(); i++) {
                                    for (int j = 0; j < chats.size(); j++) {
                                        if (FireList.get(i).getPushId().equals(chats.get(j).getPushId()) &&
                                                FireList.get(i).getTime() == chats.get(j).getTime() &&
                                                FireList.get(i).getType().equals(chats.get(j).getType()) &&
                                                FireList.get(i).getMessage().equals(chats.get(j).getMessage()) &&
                                                FireList.get(i).getSender().equals(chats.get(j).getSender()) &&
                                                FireList.get(i).getReceiver().equals(chats.get(j).getReceiver())) {
                                            FireList.remove(i);
                                            Log.e("Remove = ", FireList.get(i).toString());
                                        }
                                    }
                                }

                            } catch (IndexOutOfBoundsException | NullPointerException e) {
                                e.printStackTrace();
                            } finally {
                                for (Chat chat : FireList) {
                                    if (chat.getSender().equals(fuser.getUid()) && chat.getReceiver().equals(userid) ||
                                            chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(userid)) {
                                        addChatViewModel.insert(chat);
                                    }
                                }
                            }
                            ShowMessage(chats);

                        }
                    });*/
                }
            });
        }

        addChatViewModel.getAllChatDatabase().observe(this, new Observer<List<Chat>>() {
            @Override
            public void onChanged(List<Chat> chats) {
                Log.e("Test",chats.toString());
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Chat chat = messageAdapter.getMessage(position);
                addChatViewModel.delete(chat);
                try {
                    firestore.collection("ChatList").document(chat.getPushId()).delete();
                    FirebaseDatabase.getInstance().getReference("Chats").child(chat.getPushId()).removeValue();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }).attachToRecyclerView(recyclerView);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfo();
            }
        });
        try {
            if (getIntent().getStringExtra("imageFriend").equals("default")) {
                profile_image.setImageResource(R.mipmap.ic_launcher);
            } else {
                //and this
                Glide.with(getApplicationContext()).load(getIntent().getStringExtra("imageFriend")).into(profile_image);
            }
        } catch (NullPointerException e) {

        }

        //ListenForRecord must be false ,otherwise onClick will not be called
        sendRecord.setOnRecordClickListener(new OnRecordClickListener() {
            @Override
            public void onClick(View v) {
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
                    attach_file.setVisibility(View.VISIBLE);
                } else {
                    layout_text.setVisibility(View.VISIBLE);
                    sendRecord.setListenForRecord(true);
                    //IMPORTANT
                    sendRecord.setVisibility(View.GONE);
                    sendText.setVisibility(View.VISIBLE);
                    recordView.setVisibility(View.GONE);
                    attach_file.setVisibility(View.GONE);
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
        // to read message from realtime database
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                if (user.getImageURL().equals("default")) {
//                    profile_image.setImageResource(R.mipmap.ic_launcher);
//                } else {
//                    //and this
//                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
//                }
//                readMesagges(fuser.getUid(), userid);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        readMesagges(fuser.getUid(), userid); // not here.
        seenMessage(userid);
        PermissionHandler();
        attach_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFiles();
            }
        });


    }

    List<Chat> chatList = new ArrayList<>();

    private void ShowMessage(List<Chat> chats) {
        chatList.clear();
        for (Chat chat : chats) {
            if (chat.getSender().equals(fuser.getUid()) && chat.getReceiver().equals(userid)
                    ||
                    chat.getSender().equals(userid) && chat.getReceiver().equals(fuser.getUid())) {
                chatList.add(chat);
            }
        }
        messageAdapter = new MessageAdapter(ChatActivity.this, chatList);
        recyclerView.setAdapter(messageAdapter);
    }

    private void openFiles() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_Code);

    }

    private boolean PermissionHandler() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.RECORD_AUDIO)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        sendRecord.setEnabled(true);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        sendRecord.setEnabled(false);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
        boolean recordPermissionAvailable = ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.RECORD_AUDIO) == PERMISSION_GRANTED;
        if (recordPermissionAvailable) {
            return true;
        }
        ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
        return false;
    }

    private void seenMessage(final String friendid) {
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(friendid)) {
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

        // on Fire store
        listenerRegistration = firestore.collection("ChatList").orderBy("time").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                    Chat messageModel = documentSnapshot.toObject(Chat.class);
                    if (messageModel.getSender().equals(fuser.getUid()) && messageModel.getReceiver().equals(friendid) ||
                            messageModel.getReceiver().equals(fuser.getUid()) && messageModel.getSender().equals(friendid)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        documentSnapshot.getReference().update(hashMap);
//                        documentSnapshot.getData().put("isseen",true);

                    }
                }

            }
        });


    }

    private void sendMessage(String sender, final String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Date date = new Date();
        HashMap<String, Object> hashMap = new HashMap<>();
//        String pushId = reference.push().getKey().toString();
        String pushId = fuser.getUid() + date.getTime();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("type", "text");
        hashMap.put("isseen", false);
        hashMap.put("pushId", pushId);
        hashMap.put("time", date.getTime());
//        addChatViewModel.insert(new Chat(sender, receiver, message, "text", pushId, date.getTime()));
        reference.child("Chats").child(pushId).setValue(hashMap);
        // add user to chat fragment
        firestore.collection("ChatList").document(pushId).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                sendNotifiaction(receiver, MyName, message);
            }
        });
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

    private void currentUser(String userid) {
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();
    }

    private void status(String status) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.keepSynced(true);
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
        try {
            reference.removeEventListener(seenListener);
            listenerRegistration.remove();
        } catch (NullPointerException e) {

        }
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
            sendImage(fuser.getUid(), userid, data.getData());
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void sendImage(String sender, String FriendId, Uri imageUri) {
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
                        HashMap<String, Object> hashMap = new HashMap<>();
                        Date date = new Date();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        reference.keepSynced(true);
//                        String pushId = reference.push().getKey().toString();
                        String pushId = fuser.getUid() + date.getTime();
                        hashMap.put("sender", sender);
                        hashMap.put("receiver", FriendId);
                        hashMap.put("message", mUri);
                        hashMap.put("type", "image");
                        hashMap.put("isseen", false);
                        hashMap.put("pushId", pushId);
                        hashMap.put("time", date.getTime());
                        reference.child("Chats").child(pushId).setValue(hashMap);
//                        addChatViewModel.insert(new Chat(sender, FriendId, mUri, "image", pushId, date.getTime()));
                        firestore.collection("ChatList").document(pushId).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                sendNotifiaction(userid, MyName, "image");
                            }
                        });

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
        Intent intent = new Intent(this, UserInfoActivity.class);
        try {
            intent.putExtra("nameFriend", getIntent().getStringExtra("nameFriend").toString());
            intent.putExtra("whtatsapp", getIntent().getStringExtra("whtatsapp").toString());
            intent.putExtra("imageFriend", getIntent().getStringExtra("imageFriend").toString());
            intent.putExtra("BioFriend", getIntent().getStringExtra("BioFriend").toString());
            intent.putExtra("CountryFriend", getIntent().getStringExtra("CountryFriend").toString());
            intent.putExtra("StatusFriend", getIntent().getStringExtra("StatusFriend").toString());
        } catch (NullPointerException e) {

        }
        startActivity(intent);
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
                            Date date = new Date();
//                            String pushId = databaseReference.push().getKey().toString();
                            String pushId = fuser.getUid() + date.getTime();
                            map.put("sender", fuser.getUid());
                            map.put("receiver", userid);
                            map.put("message", url);
                            map.put("type", "voice");
                            map.put("isseen", false);
                            map.put("pushId", pushId);
                            map.put("time", date.getTime());
                            firestore.collection("ChatList").document(pushId).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    sendNotifiaction(userid, MyName, "voice");
//                                    addChatViewModel.insert(new Chat(fuser.getUid(), userid, url, "voice", pushId, date.getTime()));
                                }
                            });
                            databaseReference.child(pushId).setValue(map);
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

                        }
                    }
                });
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // we can't becouse null pointer exaption
//        chatsViewModel.resetAllMessages();
//        addChatViewModel.getAllChatDatabase().removeObservers(this);

    }

    private String getFilePath() {
        ContextWrapper contextWrapper = new ContextWrapper(this);
        File file = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File f = new File(file, "Xteam.mp3");
        return f.getPath();
    }

    @Override
    public void onClick(AttachmentOption attachmentOption) {
        switch (attachmentOption.getId()) {
            case AttachmentOption.DOCUMENT_ID:
                showToast("Document Clicked");
                break;
            case AttachmentOption.CAMERA_ID:
                showToast("Camera Clicked");
                break;
            case AttachmentOption.GALLERY_ID:
                showToast("Gallery Clicked");
                break;
            case AttachmentOption.AUDIO_ID:
                showToast("Audio Clicked");
                break;
            case AttachmentOption.LOCATION_ID:
                showToast("Location Clicked");
                break;
            case AttachmentOption.CONTACT_ID:
                showToast("Contact Clicked");
                break;
        }
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}