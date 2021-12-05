package com.doubleclick.x_course.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doubleclick.x_course.Model.Chat;
import com.doubleclick.x_course.Repositorys.ChatsRepository;

import java.util.List;

public class ChatsViewModel  extends ViewModel implements ChatsRepository.OnMessageAdded {


    MutableLiveData<List<Chat>> mutableLiveData = new MutableLiveData<>();
    ChatsRepository repository = new ChatsRepository(this);

    public ChatsViewModel() {
    }

    public void getMessageFromFireSTORE(String friendid) {
        repository.getAllMessages(friendid);
    }

    public void resetAllMessages() {
        mutableLiveData.postValue(null);
    }

    public LiveData<List<Chat>> ReturnMyMessages() {
        return  mutableLiveData;
    }

    @Override
    public void MessagesListener(List<Chat> messageModels) {
        mutableLiveData.setValue(messageModels);
    }

}
