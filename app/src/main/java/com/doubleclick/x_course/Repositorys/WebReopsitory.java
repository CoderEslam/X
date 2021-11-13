package com.doubleclick.x_course.Repositorys;

import com.doubleclick.x_course.Model.Diploma;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class WebReopsitory {

    private DatabaseReference referenceWeb;
    private ArrayList<Diploma> diplomas = new ArrayList<>();
    WebListener webListener;

    public WebReopsitory(WebListener webListener) {
        this.webListener = webListener;
    }


    public void getWebDate() {
        referenceWeb = FirebaseDatabase.getInstance().getReference().child("Web");
        referenceWeb.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Diploma diploma = dataSnapshot.getValue(Diploma.class);
                    diplomas.add(diploma);
                    webListener.loadWebData(diplomas);
                }
            }
        });
    }


    public interface WebListener {
        void loadWebData(ArrayList<Diploma> diplomaArrayList);
    }

}
