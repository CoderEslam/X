package com.doubleclick.x_course.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.doubleclick.x_course.Model.Diploma;
import com.doubleclick.x_course.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PutEmailFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    private DatabaseReference reference, referenceEmails;
    private ArrayList<String> diplomaNameArrayList = new ArrayList<>();
    private EditText email, num_diploma;
    private String nameDiplomaSpinner;
    private String[] courses;
    private Button done;
    private Spinner spinnerTrack, spin;
    private LinearLayout layoutTrack;
    private int type;
    private String track;

    public PutEmailFragment() {
        // Required empty public constructor
    }


    public static PutEmailFragment newInstance(String param1, String param2) {
        PutEmailFragment fragment = new PutEmailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reference = FirebaseDatabase.getInstance().getReference();
        referenceEmails = FirebaseDatabase.getInstance().getReference().child("Emails");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_put_email, container, false);
        spin = view.findViewById(R.id.spinner);
        spinnerTrack = view.findViewById(R.id.spinnerTrack);
        email = view.findViewById(R.id.email);
        num_diploma = view.findViewById(R.id.num_diploma);
        done = view.findViewById(R.id.done);
        spin.setOnItemSelectedListener(this);
        courses = getResources().getStringArray(R.array.Diploma);
        layoutTrack = view.findViewById(R.id.layoutTrack);
        ArrayAdapter coursesAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, courses);
        coursesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(coursesAdapter);



        /*reference.child("AllDiplomas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Diploma namediploma = dataSnapshot.getValue(Diploma.class);
                    diplomaNameArrayList.add(namediploma.getNameDiploma());
                }
                //Creating the ArrayAdapter instance having the country list
//                ArrayAdapter aa = new ArrayAdapter( getContext(), android.R.layout.simple_spinner_item, diplomaName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        done.setOnClickListener(Clicked -> {
            Map<String, Object> map = new HashMap<>();
            if (!email.getText().toString().isEmpty() && !num_diploma.getText().toString().isEmpty()) {
                map.put("diploma", nameDiplomaSpinner);
                map.put("email", email.getText().toString());
                map.put("numberOfDiploma", num_diploma.getText().toString());
                map.put("type", type);
                map.put("track",track);
                referenceEmails.push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        email.setText("");
                        num_diploma.setText("");
                        Toast.makeText(getContext(), "Done", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        nameDiplomaSpinner = courses[i];
        type = i;
//        Toast.makeText(getContext(),""+nameDiplomaSpinner,Toast.LENGTH_LONG).show();
        if (!nameDiplomaSpinner.isEmpty()) {
            if (nameDiplomaSpinner.equals("Web")) {
                String[] Track = getResources().getStringArray(R.array.Web);
                ArrayAdapter TrackAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, Track);
                spinnerTrack.setAdapter(TrackAdapter);
                spinnerTrack.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        track = "";
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        track = "";
                    }
                });
            } else if (nameDiplomaSpinner.equals("Mobile")) {
                String[] Track = getResources().getStringArray(R.array.Mobile);
                ArrayAdapter TrackAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, Track);
                spinnerTrack.setAdapter(TrackAdapter);
                spinnerTrack.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        track = Track[i];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        track = Track[0];
                    }
                });
            } else if (nameDiplomaSpinner.equals("graphicDesign")) {
                String[] Track = getResources().getStringArray(R.array.graphicDesign);
                ArrayAdapter TrackAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, Track);
                spinnerTrack.setAdapter(TrackAdapter);
                spinnerTrack.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        track = Track[i];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        track = Track[0];
                    }
                });
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        nameDiplomaSpinner = courses[0];
    }


}