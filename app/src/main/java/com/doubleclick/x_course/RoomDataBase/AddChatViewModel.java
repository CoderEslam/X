package com.doubleclick.x_course.RoomDataBase;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.doubleclick.x_course.Model.Chat;

import java.util.List;

public class AddChatViewModel extends AndroidViewModel {

    private ChatRepositry mRepositry;
    private Context context;
    private LiveData<List<Chat>> allChat;

    public AddChatViewModel(Application application) {
        super(application);
       context = application;
    }
    public void getAllChats(String myID,String FriendId){
        mRepositry = new ChatRepositry(context, myID, FriendId);
        allChat = mRepositry.getAllChats();
    }

    public void insert(Chat chat) {
        mRepositry.insert(chat);

    }

    public void update(Chat chat) {
        mRepositry.update(chat);

    }

    public void delete(Chat chat) {
        mRepositry.delete(chat);

    }

    public void deleteAllData(){
        mRepositry.deleteAllWords();
    }

    public LiveData<List<Chat>> getAllChatDatabase() {
        return allChat;
    }

}
