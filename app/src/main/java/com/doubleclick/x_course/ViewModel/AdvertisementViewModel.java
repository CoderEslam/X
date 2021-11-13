package com.doubleclick.x_course.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doubleclick.x_course.Model.Advertisement;
import com.doubleclick.x_course.Repositorys.AdvertisementRepository;

import java.util.ArrayList;

public class AdvertisementViewModel extends ViewModel implements AdvertisementRepository.OnClickListener {


    AdvertisementRepository advertisementRepository = new AdvertisementRepository(this);
    MutableLiveData<ArrayList<Advertisement>> advertisementMutableLiveData = new MutableLiveData<>();

    public AdvertisementViewModel() {
        advertisementRepository.LoadAdvertisement();
    }

    public LiveData<ArrayList<Advertisement>> LoadAdv(){
        return advertisementMutableLiveData;
    }


    @Override
    public void onClick(ArrayList<Advertisement> advertisement) {
        advertisementMutableLiveData.setValue(advertisement);
    }
}
