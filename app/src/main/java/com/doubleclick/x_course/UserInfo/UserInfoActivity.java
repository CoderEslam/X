package com.doubleclick.x_course.UserInfo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.doubleclick.x_course.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends AppCompatActivity {

    String IdFriend;
    CircleImageView profile_image_profile;
    TextView country, Bio, Status, whatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        IdFriend = getIntent().getStringExtra("IdFriend");
        Toolbar toolbar = findViewById(R.id.toolbar);
        profile_image_profile = findViewById(R.id.profile_image_profile);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbarlayout);
        try {
            toolBarLayout.setTitle(getIntent().getStringExtra("nameFriend"));
            Glide.with(UserInfoActivity.this).load(getIntent().getStringExtra("imageFriend")).placeholder(R.drawable.account_circle_24).into(profile_image_profile);
            whatsapp = findViewById(R.id.whatsapp);
            country = findViewById(R.id.Country);
            Bio = findViewById(R.id.Bio);
            Status = findViewById(R.id.Status);
            whatsapp.setText(getIntent().getStringExtra("whtatsapp"));
            country.setText(getIntent().getStringExtra("CountryFriend"));
            Bio.setText(getIntent().getStringExtra("BioFriend"));
            Status.setText(getIntent().getStringExtra("StatusFriend"));
        }catch (NullPointerException e){

        }


    }
}