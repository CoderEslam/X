package com.doubleclick.x_course.Repositorys;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.doubleclick.x_course.CustomNavigation.MainFragment;
import com.doubleclick.x_course.Model.User;
import com.doubleclick.x_course.NavigationDrawerActivity;
import com.doubleclick.x_course.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

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
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(UserId);

    }

    public void LoadUserData(){
        if (mAuth!=null){
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user = snapshot.getValue(User.class);
                        if (user.getEmail() != null) {
                            userListener.userListener(user);
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
