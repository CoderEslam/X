package com.doubleclick.x_course.RoomDataBase;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.doubleclick.x_course.Model.Chat;

import java.util.List;

@Dao //DataAccessObject
public interface ChatDAO {


    @Insert
    void insert(Chat chat);

    @Update
    void update(Chat chat);

    @Delete
    void delete(Chat chat);

    @Query("DELETE FROM Chat")
    void deleteAllData();

    @Query("SELECT * FROM Chat  WHERE (sender == :myID   AND receiver == :FriendId) OR (sender == :FriendId AND receiver == :myID) ORDER BY time ASC")
    LiveData<List<Chat>> getAllChat(String myID, String FriendId);

//    @Query("SELECT * FROM Chat WHERE sender==Id Or receiver == ")//
//    LiveData<List<Chat>> getMTCon();

}
