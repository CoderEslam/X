package com.doubleclick.x_course.Repositorys;

import androidx.annotation.NonNull;

import com.doubleclick.x_course.PyChat.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserRespository {

    private DatabaseReference reference;
    UserListener userListener;
    private FirebaseAuth mAuth;
    private String UserId;
    private User user;

    public UserRespository(UserListener userListener) {
        this.userListener = userListener;
        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getUid().toString();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

    }

    public void LoadUserData(String Id){
        if (mAuth!=null){
            reference.child(Id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user = snapshot.getValue(User.class);
                    try {
                        if (user.getId() != null) {
                            userListener.userListener(user);
                        }
                    }catch (NullPointerException e){

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }


    public interface UserListener{
        void userListener(User user);
    }

}
