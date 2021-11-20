package com.doubleclick.x_course.Repositorys;

import androidx.annotation.NonNull;

import com.doubleclick.x_course.Model.ItemCourse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemCourseRepository {

    private DatabaseReference reference;
    ItemListener itemListener;
    ArrayList<ItemCourse> itemListeners = new ArrayList<>();

    public ItemCourseRepository(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public void getItems(){

        reference = FirebaseDatabase.getInstance().getReference().child("ItemCourse");
        reference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ItemCourse itemCourse = dataSnapshot.getValue(ItemCourse.class);
                    itemListeners.add(itemCourse);
                    itemListener.mListener(itemListeners);
                }
            }
        });

    }

    public interface ItemListener{
        void mListener(ArrayList<ItemCourse> itemCourse);
    }


}
