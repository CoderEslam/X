package com.doubleclick.x_course.Repositorys;

import com.doubleclick.x_course.Model.Advertisement;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdvertisementRepository {

    OnClickListener onClickListener;
    private DatabaseReference referenceAdvertisement;
    private ArrayList<Advertisement> advertisements = new ArrayList<>();

    public AdvertisementRepository(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void LoadAdvertisement(){
        referenceAdvertisement= FirebaseDatabase.getInstance().getReference().child("Advertisement");
        referenceAdvertisement.keepSynced(true);
        referenceAdvertisement.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Advertisement advertisement = dataSnapshot.getValue(Advertisement.class);
                    advertisements.add(advertisement);
                }
                onClickListener.onClick(advertisements);
            }
        });
    }

    public interface OnClickListener{
        void onClick(ArrayList<Advertisement> advertisement);
    }

}
