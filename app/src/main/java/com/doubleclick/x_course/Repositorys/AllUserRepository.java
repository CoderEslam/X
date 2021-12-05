package com.doubleclick.x_course.Repositorys;

import androidx.annotation.NonNull;

import com.doubleclick.x_course.PyChat.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AllUserRepository {

    DatabaseReference reference;
    private List<User> mUsers = new ArrayList<>();
    private FirebaseAuth mAuth;
    private String myId;
    private AllUsersListener allUsersListener;

    public AllUserRepository(AllUsersListener allUsersListener) {
        this.allUsersListener = allUsersListener;
    }

    public void loadAllUsers() {
        mAuth = FirebaseAuth.getInstance();
        myId = mAuth.getCurrentUser().getUid().toString();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.keepSynced(true);
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (!user.getId().equals(myId)) {
                        mUsers.add(user);
                    }
                }
                allUsersListener.UsersListener(mUsers);
            }
        });
    }

    public interface AllUsersListener {
        void UsersListener(List<User> users);
    }

}
