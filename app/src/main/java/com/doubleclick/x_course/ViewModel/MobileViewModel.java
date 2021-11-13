package com.doubleclick.x_course.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doubleclick.x_course.Model.Diploma;
import com.doubleclick.x_course.Repositorys.MobileReopsitory;

import java.util.ArrayList;

public class MobileViewModel extends ViewModel implements MobileReopsitory.MobileListener {


    MobileReopsitory mobileReopsitory = new MobileReopsitory(this);
    MutableLiveData mutableLiveData = new MutableLiveData();

    public MobileViewModel() {
        mobileReopsitory.getMobileDate();
    }

    public LiveData<ArrayList<Diploma>> getMobileData() {
        return mutableLiveData;
    }

    @Override
    public void loadMobileData(ArrayList<Diploma> diplomaArrayList) {
        mutableLiveData.setValue(diplomaArrayList);
    }
}
