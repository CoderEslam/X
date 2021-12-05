package com.doubleclick.x_course.PyChat.interfaces;

import android.view.View;

import com.doubleclick.x_course.PyChat.models.Group;
import com.doubleclick.x_course.PyChat.models.User;


public interface ChatItemClickListener {
    void onChatItemClick(String chatId, String chatName, int position, View userImage);

    void onChatItemClick(Group group, int position, View userImage);

    void placeCall(boolean callIsVideo, User user);
}
