package com.doubleclick.x_course.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.doubleclick.x_course.Model.User;
import com.doubleclick.x_course.Repositorys.UserRespository;



public class UserViewModel extends ViewModel implements UserRespository.UserListener {


    MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    UserRespository userRespository = new UserRespository(this);

    public UserViewModel() {
        userRespository.LoadUserData();
    }

    public LiveData<User> getUserLiveData() {
        return userMutableLiveData;
    }

    @Override
    public void userListener(User user) {
        userMutableLiveData.setValue(user);
    }
}
