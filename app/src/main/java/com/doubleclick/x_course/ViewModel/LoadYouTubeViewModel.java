package com.doubleclick.x_course.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doubleclick.x_course.Model.YouTubeDataModel;
import com.doubleclick.x_course.Repositorys.LoadDataRepository;

import java.util.ArrayList;

public class LoadYouTubeViewModel extends ViewModel implements LoadDataRepository.OnItemClickListener {

    MutableLiveData<ArrayList<YouTubeDataModel>> arrayListMutableLiveData = new MutableLiveData<>();

    public void Getter(String name, String number, String email) {
        LoadDataRepository loadDataRepository = new LoadDataRepository(this, name, number, email);
        loadDataRepository.loadUserData();
    }


    public LoadYouTubeViewModel() {
    }


    public LiveData<ArrayList<YouTubeDataModel>> getData() {
        return arrayListMutableLiveData;
    }

    @Override
    public void onItemClick(ArrayList<YouTubeDataModel> youTubeDataModels) {
        arrayListMutableLiveData.setValue(youTubeDataModels);
    }

}
