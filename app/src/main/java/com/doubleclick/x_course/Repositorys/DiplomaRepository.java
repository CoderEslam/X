package com.doubleclick.x_course.Repositorys;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.doubleclick.x_course.Adapter.DiplomasAdapter;
import com.doubleclick.x_course.Model.Diploma;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DiplomaRepository {

    Listener InterfaceListener;
    private DatabaseReference referenceAllPlayList;
    private ArrayList<Diploma> diplomasArrayList = new ArrayList<>();

    public DiplomaRepository(Listener interfaceListener) {
        InterfaceListener = interfaceListener;
    }


    public ArrayList<Diploma> AllDiploma() {
        diplomasArrayList = new ArrayList<>();
        referenceAllPlayList = FirebaseDatabase.getInstance().getReference().child("AllPlayLists");
        referenceAllPlayList.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Diploma diploma = dataSnapshot.getValue(Diploma.class);
                    diplomasArrayList.add(diploma);
                    InterfaceListener.ClickListener(diplomasArrayList);
                }
               /* for (int i = 1; i < 1000; i++) { // TODO => 100 indicate to the numbers of all diplomas
//                    Toast.makeText(getContext(), "getChildrenCount = " + snapshot.getChildrenCount(), Toast.LENGTH_LONG).show();
                    if (snapshot.child("Mobile").hasChild("Mobile" + i)) {
                        referenceAllPlayList.child("Mobile")
                                .child("Mobile" + i)
                                .child("Mobile").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                Diploma diplomaMobile = dataSnapshot.getValue(Diploma.class);
//                                        Toast.makeText(context, "  = " + diplomaMobile.toString(), Toast.LENGTH_LONG).show();
                                diplomasArrayList.add(diplomaMobile);
                                InterfaceListener.ClickListener(diplomasArrayList);
//                                        liveData.setValue(diplomasArrayList);
                            }
                        });

                    }
                    if (snapshot.child("Web").hasChild("Web" + i)) {
                        referenceAllPlayList.child("Web")
                                .child("Web" + i)
                                .child("Web").get()
                                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                    @Override
                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                        Diploma diplomaWeb = dataSnapshot.getValue(Diploma.class);
//                                        Toast.makeText(getContext()," = "+diplomaWeb.toString(),Toast.LENGTH_LONG).show();
                                        diplomasArrayList.add(diplomaWeb);
                                        InterfaceListener.ClickListener(diplomasArrayList);

//                                        liveData.setValue(diplomasArrayList);
                                    }
                                });
                    }
                    if (snapshot.child("graphicDesign").hasChild("graphicDesign" + i)) {
                        referenceAllPlayList.child("graphicDesign")
                                .child("graphicDesign" + i)
                                .child("graphicDesign").get()
                                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                    @Override
                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                        Diploma diplomagraphicDesign = dataSnapshot.getValue(Diploma.class);
                                        diplomasArrayList.add(diplomagraphicDesign);
                                        InterfaceListener.ClickListener(diplomasArrayList);
                                    }
                                });
                    }
                }*/
            }
        });
        return diplomasArrayList;
    }

    public interface Listener {
        void ClickListener(ArrayList<Diploma> diplomas);
    }

}
