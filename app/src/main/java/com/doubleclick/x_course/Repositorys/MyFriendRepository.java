package com.doubleclick.x_course.Repositorys;

import androidx.annotation.NonNull;

import com.doubleclick.x_course.Model.Chatlist;
import com.doubleclick.x_course.Notifications.Token;
import com.doubleclick.x_course.PyChat.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class MyFriendRepository {

    DatabaseReference reference;
    FirebaseAuth mAuth;
    private String myId;
    private List<Chatlist> usersList = new ArrayList<>();
    private List<User> mUsers = new ArrayList<>();
    MyFriendListener myFriendListener;

    public MyFriendRepository(MyFriendListener myFriendListener) {
        this.myFriendListener = myFriendListener;
    }

    public void LoadAllFriend() {
        mAuth = FirebaseAuth.getInstance();
        myId = mAuth.getCurrentUser().getUid().toString();
        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(myId);
        reference.keepSynced(true);
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }

                chatList();
            }
        });
        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(myId).setValue(token1);
    }

    private void chatList() {
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.keepSynced(true);
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();

                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    for (Chatlist chatlist : usersList) {
                        if (user.getId().equals(chatlist.getId())) {
                            mUsers.add(user);
                        }
                    }
                }
                myFriendListener.myFriendListener(mUsers);
            }
        });
    }

    public interface MyFriendListener {
        void myFriendListener(List<User> users);
    }


}
