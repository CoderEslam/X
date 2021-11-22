package com.doubleclick.x_course.Repositorys;

import com.doubleclick.x_course.Model.Diploma;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MobileReopsitory {

    private DatabaseReference referenceMob;
    private ArrayList<Diploma> diplomas = new ArrayList<>();
    MobileListener mobileListener;

    public MobileReopsitory(MobileListener mobileListener) {
        this.mobileListener = mobileListener;
    }

    public void getMobileDate() {
        referenceMob = FirebaseDatabase.getInstance().getReference().child("Mobile");
        referenceMob.keepSynced(true);
        referenceMob.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Diploma diploma = dataSnapshot.getValue(Diploma.class);
                    diplomas.add(diploma);
                    mobileListener.loadMobileData(diplomas);
                }
            }
        });
    }


    public interface MobileListener {
        void loadMobileData(ArrayList<Diploma> diplomaArrayList);
    }

}
