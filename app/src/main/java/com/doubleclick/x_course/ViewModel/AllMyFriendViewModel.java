package com.doubleclick.x_course.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doubleclick.x_course.PyChat.models.User;
import com.doubleclick.x_course.Repositorys.MyFriendRepository;

import java.util.List;

public class AllMyFriendViewModel extends ViewModel implements MyFriendRepository.MyFriendListener {

    MutableLiveData<List<User>> mutableLiveData = new MutableLiveData<>();

    MyFriendRepository myFriendRepository = new MyFriendRepository(this);

    public AllMyFriendViewModel() {
        myFriendRepository.LoadAllFriend();
    }

    public LiveData<List<User>> getLiveData() {
        return mutableLiveData;
    }

    @Override
    public void myFriendListener(List<User> users) {
        mutableLiveData.setValue(users);
    }
}
