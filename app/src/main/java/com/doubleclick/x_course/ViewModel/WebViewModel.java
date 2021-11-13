package com.doubleclick.x_course.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doubleclick.x_course.Model.Diploma;
import com.doubleclick.x_course.Repositorys.WebReopsitory;

import java.util.ArrayList;

public class WebViewModel extends ViewModel implements WebReopsitory.WebListener {

    MutableLiveData<ArrayList<Diploma>> mutableLiveData = new MutableLiveData<>();
    WebReopsitory webReopsitory = new WebReopsitory(this);

    public WebViewModel() {
        webReopsitory.getWebDate();
    }

    public LiveData<ArrayList<Diploma>> getWebData(){
        return mutableLiveData;
    }


    @Override
    public void loadWebData(ArrayList<Diploma> diplomaArrayList) {
        mutableLiveData.setValue(diplomaArrayList);
    }
}
