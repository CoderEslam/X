package com.doubleclick.x_course.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.doubleclick.x_course.Adapter.AdminViewPager;
import com.doubleclick.x_course.Advertisement.AdvertisementActivity;
import com.doubleclick.x_course.Model.Diploma;
import com.doubleclick.x_course.R;
import com.doubleclick.x_course.RequstesActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private DatabaseReference referenceAllDiplomas;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AlertDialog.Builder builder;
    private View view;
    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        builder = new AlertDialog.Builder(this);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        referenceAllDiplomas = FirebaseDatabase.getInstance().getReference().child("AllDiplomas");
        //set the abs title
        tabLayout.addTab(tabLayout.newTab().setText("Put Email"));
        tabLayout.addTab(tabLayout.newTab().setText("Create Diploma"));
        AdminViewPager pagerAdapter = new AdminViewPager(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //to put Fragment on ViewPager when I selected on the tab
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        referenceAllDiplomas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Diploma diploma = dataSnapshot.getValue(Diploma.class);
                    list.add(diploma.getNameOfDiploma());
                }

//                ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, diplomaNameArrayList);
//                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                //Setting the ArrayAdapter data on the Spinner
//                spin.setAdapter(aa);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.requests:
                Intent intent = new Intent(AdminActivity.this, RequstesActivity.class);
                startActivity(intent);
                return true;
            case R.id.Adv:
                Intent Adv = new Intent(AdminActivity.this, AdvertisementActivity.class);
                startActivity(Adv);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}