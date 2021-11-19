package com.doubleclick.x_course.LogIn;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.doubleclick.x_course.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgetpasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgetpasswordFragment extends Fragment {

    private TextView tv_email ,tv_goBack;
    private ImageView Iv_email;
    private ProgressBar emailprogressBar;
    private Button emailbtn;
    private EditText et_email_forgetpassword;
    private FrameLayout frameLayout;
    private FirebaseAuth firebaseAuth;
    private ViewGroup viewGroupPassword;


    public ForgetpasswordFragment() {
        // Required empty public constructor
    }

    public static ForgetpasswordFragment newInstance(String param1, String param2) {
        ForgetpasswordFragment fragment = new ForgetpasswordFragment();
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
        View view =  inflater.inflate(R.layout.fragment_forgetpassword, container, false);
        frameLayout = getActivity().findViewById(R.id.register_frameLayout);
        tv_email = view.findViewById(R.id.tv_email);
        Iv_email = view.findViewById(R.id.Iv_email);
        viewGroupPassword = view.findViewById(R.id.forget_password_email);
        et_email_forgetpassword = view.findViewById(R.id.et_email_forgetpassword);
        emailprogressBar = view.findViewById(R.id.forgetprogressBar);
        emailbtn = view.findViewById(R.id.btn_forgetpassword);
        tv_goBack = view.findViewById(R.id.tv_goBack);
        firebaseAuth  = FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SigninFragment());
            }
        });
        et_email_forgetpassword.addTextChangedListener(new TextWatcher() {
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

        emailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailprogressBar.setVisibility(View.VISIBLE);
                emailbtn.setEnabled(false);
                emailbtn.setTextColor(Color.argb(50,250,250,250));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(viewGroupPassword);
                }
                firebaseAuth.sendPasswordResetEmail(et_email_forgetpassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
//                            tv_email.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.sample_anim));
                                    Iv_email.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.sample_anim));
                                    //is equal under
                                    ScaleAnimation scaleAnimation = new ScaleAnimation(1,0,1,0);
                                    scaleAnimation.setDuration(100);
                                    scaleAnimation.setInterpolator(new AccelerateInterpolator());
                                    scaleAnimation.setRepeatMode(Animation.REVERSE);
                                    scaleAnimation.setRepeatCount(1);
                                    scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {

                                        }
                                    });
                                    tv_email.startAnimation(scaleAnimation);

                                    tv_email.setVisibility(View.VISIBLE);
                                    Iv_email.setVisibility(View.VISIBLE);
                                    emailprogressBar.setVisibility(View.INVISIBLE);
                                }else {
                                    emailprogressBar.setVisibility(View.INVISIBLE);
                                    String error = task.getException().getMessage();
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                        TransitionManager.beginDelayedTransition(viewGroupPassword);
                                    }
                                    Iv_email.setVisibility(View.INVISIBLE);
                                    tv_email.setTextColor(Color.rgb(236,30,30));
                                    tv_email.setVisibility(View.VISIBLE);
                                    tv_email.setText(error);
                                }
                            }
                        });
            }
        });
    }

    private void CheckInputs() {
        if (TextUtils.isEmpty(et_email_forgetpassword.getText())){
            emailbtn.setEnabled(false);
            emailbtn.setTextColor(Color.argb(50,250,250,250));
        }else {
            emailbtn.setEnabled(true);
            emailbtn.setTextColor(Color.rgb(250,250,250));
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        // to change between fragment with animation
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_right);
        //replace first fragment (frameLayout.getId()) with SigninFragment
        fragmentTransaction.replace(R.id.register_frameLayout,fragment);
        //commit == execute
        fragmentTransaction.commit();
    }

}