package com.doubleclick.x_course.ViewModel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doubleclick.x_course.Adapter.DiplomasAdapter;
import com.doubleclick.x_course.Model.Diploma;
import com.doubleclick.x_course.Repositorys.DiplomaRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainViewModel extends ViewModel implements DiplomaRepository.Listener {

    private static MutableLiveData<ArrayList<Diploma>> mutableLiveData = new MutableLiveData<>();

    private DiplomaRepository diplomaRepository = new DiplomaRepository(this);

    public MainViewModel() {
        diplomaRepository.AllDiploma();
    }

    public LiveData<ArrayList<Diploma>> getLiveData() {
        return mutableLiveData;
    }

    @Override
    public void ClickListener(ArrayList<Diploma> diplomas) {
        mutableLiveData.setValue(diplomas);
    }
}
