package com.doubleclick.x_course.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doubleclick.x_course.PyChat.models.User;
import com.doubleclick.x_course.Repositorys.AllUserRepository;

import java.util.List;

public class AllUsersViewModel extends ViewModel implements AllUserRepository.AllUsersListener {

    MutableLiveData<List<User>> mutableLiveData = new MutableLiveData<>();

    AllUserRepository allUserRepository = new AllUserRepository(this);

    public AllUsersViewModel() {
        allUserRepository.loadAllUsers();
    }


    public LiveData<List<User>> getAllUsersLiveData() {
        return mutableLiveData;
    }


    @Override
    public void UsersListener(List<User> users) {
        mutableLiveData.setValue(users);
    }
}
