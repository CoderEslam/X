package com.doubleclick.x_course.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doubleclick.x_course.Model.Request;
import com.doubleclick.x_course.Repositorys.RequestRepsitory;

import java.util.ArrayList;

public class RequestViewModel extends ViewModel implements RequestRepsitory.RequestListener {


    MutableLiveData<ArrayList<Request>> RequestMutableLiveData = new MutableLiveData<>();

    RequestRepsitory requestRepsitory = new RequestRepsitory(this);

    public RequestViewModel() {
        requestRepsitory.LoadRequests();
    }

    public LiveData<ArrayList<Request>> getLiveData() {
        return RequestMutableLiveData;
    }

    @Override
    public void ClickRequestListener(ArrayList<Request> requests) {
        RequestMutableLiveData.setValue(requests);
    }
}
