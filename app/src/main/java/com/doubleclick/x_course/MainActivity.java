package com.doubleclick.x_course;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.doubleclick.x_course.LogIn.SigninFragment;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    public static boolean onResetPasswordFragment = false;
    public static boolean setSignUp = false;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        frameLayout = findViewById(R.id.register_frameLayout);

        if(setSignUp){
            setSignUp = false;
            setDefaultFragment(new SigninFragment());

        }else {
            setSignUp = true;
            setDefaultFragment(new SigninFragment());
        }
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String ID = user.getUid();
                    Intent intent = new Intent(MainActivity.this,NavigationDrawerActivity.class);
                    startActivity(intent);
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            mAuth.addAuthStateListener(firebaseAuthListener);
        }catch (NullPointerException e){

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            finish();
            mAuth.removeAuthStateListener(firebaseAuthListener);
        }catch (NullPointerException e){

        }
    }

    private void setFragment(Fragment signinFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // to change between fragment with animation
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slideout_from_right);
        //replace first fragment (frameLayout.getId()) with SigninFragment
        fragmentTransaction.replace(frameLayout.getId(),signinFragment);
        //commit == execute
        fragmentTransaction.commit();
    }
    private void setDefaultFragment(Fragment signinFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // to change between fragment with animation
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slideout_from_right);
        //replace first fragment (frameLayout.getId()) with SigninFragment
        fragmentTransaction.replace(frameLayout.getId(),signinFragment);
        //commit == execute
        fragmentTransaction.commit();
    }

//    @Override
//    public void onBackPressed() {
//        setFragment(new SignupFragment());
//        super.onBackPressed();
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode ==KeyEvent.KEYCODE_BACK){
            if (onResetPasswordFragment){
                setFragment(new SigninFragment());
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}