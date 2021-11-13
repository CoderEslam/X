package com.doubleclick.x_course.LogIn;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.doubleclick.x_course.MainActivity;
import com.doubleclick.x_course.NavigationDrawerActivity;
import com.doubleclick.x_course.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SigninFragment extends Fragment {

    private TextView dontHaveanAccount,forgetPassword,tv_Skip;
    private FrameLayout parentFrameLayout;
    private ImageView Exitbtn;
    private EditText SignInEmail,SignInPassword;
    private LottieAnimationView SignInprogressBar;
    private Button SignInbtn;
    private FirebaseAuth firebaseAuthentication;


    public SigninFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        tv_Skip = view.findViewById(R.id.tv_Skip);
        dontHaveanAccount = view.findViewById(R.id.tv_donthaveanAcount);
        parentFrameLayout = getActivity().findViewById(R.id.register_frameLayout);
        SignInEmail = view.findViewById(R.id.et_Email);
        SignInPassword = view.findViewById(R.id.et_Password);
        SignInprogressBar = view.findViewById(R.id.SignInprogressBar);
        SignInbtn = view.findViewById(R.id.btn_signup);
        firebaseAuthentication = FirebaseAuth.getInstance();
        forgetPassword = view.findViewById(R.id.tv_forget_password);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dontHaveanAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignupFragment());
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.onResetPasswordFragment = true;
                setFragment(new ForgetpasswordFragment());
            }
        });
        SignInEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        SignInPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        SignInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInWithEmailAndPassword();
            }
        });

        tv_Skip.setOnClickListener(view1 -> { SendUserToMainActivity(); });

    }

    private void SignInWithEmailAndPassword(){
        SignInprogressBar.setVisibility(View.VISIBLE);
        firebaseAuthentication.signInWithEmailAndPassword(SignInEmail.getText().toString()
                ,SignInPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            SendUserToMainActivity();
                            SignInprogressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(),"Sign In Successfully",Toast.LENGTH_LONG).show();
                        }else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getActivity(),""+error,Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
    private void CheckInputs() {
        if (!TextUtils.isEmpty(SignInEmail.getText())){
            if (!TextUtils.isEmpty(SignInPassword.getText())) {
                // TODO : when every think is okay so >> enable .
                SignInbtn.setEnabled(true);
                SignInbtn.setTextColor(getResources().getColor(R.color.darkBlue));
            }else {
                SignInbtn.setEnabled(false);
                SignInbtn.setTextColor(getResources().getColor(R.color.whiteGray));
            }
        }else {
            SignInbtn.setEnabled(false);
            SignInbtn.setTextColor(getResources().getColor(R.color.whiteGray));
        }
    }

    private void setFragment(Fragment signupFragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        //replace first fragment (frameLayout.getId()) with SigninFragment
        //setCustomAnimations(    new  ,      current     ) >>> int enter, int exit
        fragmentTransaction.setCustomAnimations(R.anim.zoomin,R.anim.zoomout);
        fragmentTransaction.replace(parentFrameLayout.getId(),signupFragment);
//        fragmentTransaction.setCustomAnimations(R.anim.bounce,R.anim.bounce);
        fragmentTransaction.commit();

    }

    private void SendUserToMainActivity(){
        Intent MainIntent = new Intent(getActivity(), NavigationDrawerActivity.class);
        startActivity(MainIntent);
        getActivity().finish();
    }


}