package com.doubleclick.x_course.Repositorys;

import com.doubleclick.x_course.Model.Diploma;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GraphicDesignReopsitory {

    private DatabaseReference referenceGraphicDesign;
    private ArrayList<Diploma> diplomas = new ArrayList<>();
    GraphicDesignListener graphicDesignListener;

    public GraphicDesignReopsitory(GraphicDesignListener graphicDesignListener) {
        this.graphicDesignListener = graphicDesignListener;
    }


    public void getGraphicDesignDate() {
        referenceGraphicDesign = FirebaseDatabase.getInstance().getReference().child("GraphicDesign");
        referenceGraphicDesign.keepSynced(true);
        referenceGraphicDesign.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Diploma diploma = dataSnapshot.getValue(Diploma.class);
                    diplomas.add(diploma);
                    graphicDesignListener.loadGraphicDesignData(diplomas);
                }
            }
        });
    }


    public interface GraphicDesignListener {
        void loadGraphicDesignData(ArrayList<Diploma> diplomaArrayList);
    }

}
