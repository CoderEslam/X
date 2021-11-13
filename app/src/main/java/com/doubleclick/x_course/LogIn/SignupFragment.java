package com.doubleclick.x_course.LogIn;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.doubleclick.x_course.NavigationDrawerActivity;
import com.doubleclick.x_course.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class SignupFragment extends Fragment {

    //    private TextView alreadyhaveanAcount;
    private FrameLayout parentFrameLayout;
    private EditText signupEmail,signupPassword,signupFullName;
    private Button signupbtn;
    private LottieAnimationView signupProgressBar;
    private ImageView Exitbtn;
    private FirebaseAuth firebaseAuthentication;
    private DatabaseReference RootdatabaseReference;
    //    private FirebaseFirestore UserFirebaseFirestore;
    private final String EMAILPATTERN = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public SignupFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_signup, container, false);

        firebaseAuthentication = FirebaseAuth.getInstance();
//        UserFirebaseFirestore = FirebaseFirestore.getInstance();
        //https://my-mall-70e39.firebaseio.com/
        RootdatabaseReference = FirebaseDatabase.getInstance().getReference();
        parentFrameLayout = getActivity().findViewById(R.id.register_frameLayout);
        signupEmail = view.findViewById(R.id.et_Email);
        signupPassword = view.findViewById(R.id.et_Password);
        signupFullName = view.findViewById(R.id.et_Name);
        signupbtn  =view.findViewById(R.id.btn_signup);
        signupProgressBar = view.findViewById(R.id.signupProgressBar);
        Exitbtn= view.findViewById(R.id.Back);

        Exitbtn.setOnClickListener(view1 -> {
            setFragment(new SigninFragment());
        });

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        alreadyhaveanAcount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setFragment(new SigninFragment());
//            }
//        });
        Exitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToMainActivity();
            }
        });

        signupEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signupPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signupFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO : send data to firebase
                CheckEmailAndPassword();
            }
        });

    }

    private void CheckEmailAndPassword() {
        Drawable errorIcon = getResources().getDrawable(R.drawable.error_24);
        errorIcon.setBounds(0,0,errorIcon.getIntrinsicWidth(),errorIcon.getIntrinsicHeight());
        if (!TextUtils.isEmpty(signupEmail.getText().toString())//.matches(EMAILPATTERN)
        ) {
            signupProgressBar.setVisibility(View.VISIBLE);
            firebaseAuthentication.createUserWithEmailAndPassword(signupEmail.getText().toString(),
                    signupPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                String UserId = mAuth.getCurrentUser().getUid().toString();
                                Map<Object,String> UserData = new HashMap<>();
                                UserData.put("username",signupFullName.getText().toString());
                                UserData.put("imageURL","default");
                                UserData.put("id",UserId);
                                UserData.put("search",signupFullName.getText().toString());
                                UserData.put("email",signupEmail.getText().toString());
                                SendUserToMainActivity();
                                String CurrentUserID = firebaseAuthentication.getCurrentUser().getUid();
                                RootdatabaseReference.child("Users").child(CurrentUserID).setValue(UserData);
//                                    RootdatabaseReference.child("Users").child(CurrentUserID).setValue("New Account Created");
//                                    RootdatabaseReference.child("USERS").child(CurrentUserID).setValue(UserData);
                                Toast.makeText(getActivity(), "Account Created Successfully", Toast.LENGTH_LONG).show();
                                signupProgressBar.setVisibility(View.INVISIBLE);

                            } else {
                                String massege = task.getException().getMessage();
                                Toast.makeText(getActivity(), "Error : " + massege, Toast.LENGTH_LONG).show();

                            }
                        }
                    });

        }
        else {
//            signupEmail.setError("Invaild Email!",errorIcon);
        }
    }

    private void SendUserToMainActivity(){
        Intent mainIntent = new Intent(getActivity(), NavigationDrawerActivity.class);
        startActivity(mainIntent);
        getActivity().finish();
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(signupEmail.getText().toString())){
            if (!TextUtils.isEmpty(signupPassword.getText()) &&signupPassword.length() >= 8){
                if (!TextUtils.isEmpty(signupFullName.getText().toString())){
                    // TODO : when every think is okay so >> enable .
                    signupbtn.setEnabled(true);
                    signupbtn.setTextColor(getResources().getColor(R.color.darkBlue));
                }else {
                    signupbtn.setTextColor(getResources().getColor(R.color.whiteGray));
                    signupbtn.setEnabled(false);
                }
            }else {
                signupbtn.setTextColor(getResources().getColor(R.color.whiteGray));
                signupbtn.setEnabled(false);
            }
        }else {
            signupbtn.setTextColor(getResources().getColor(R.color.whiteGray));
            signupbtn.setEnabled(false);
        }
    }

    private void setFragment(Fragment signinFragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        //setCustomAnimations(    new    ,     current     )
        fragmentTransaction.setCustomAnimations(R.anim.bounce,R.anim.mixed_anim);
        //replace first fragment (frameLayout.getId()) with SigninFragment
        fragmentTransaction.replace(parentFrameLayout.getId(),signinFragment);
        fragmentTransaction.commit();
    }

}