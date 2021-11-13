package com.doubleclick.x_course.Repositorys;

import androidx.annotation.NonNull;

import com.doubleclick.x_course.Adapter.RequestsAdapter;
import com.doubleclick.x_course.Model.Request;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestRepsitory {

    private DatabaseReference RequestReference;
    private ArrayList<Request> requestArrayList = new ArrayList<>();
    RequestListener requestListener;

    public RequestRepsitory(RequestListener requestListener) {
        this.requestListener = requestListener;
    }

    public void LoadRequests(){
        RequestReference = FirebaseDatabase.getInstance().getReference();
        RequestReference.child("Requests").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot Snapshot:dataSnapshot.getChildren()){
                    Request request =Snapshot.getValue(Request.class);
                    requestArrayList.add(request);
                    requestListener.ClickRequestListener(requestArrayList);
                }
            }
        });
    }



    public interface RequestListener {
        void ClickRequestListener(ArrayList<Request> requests);
    }

}
