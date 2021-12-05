package com.doubleclick.x_course.PyChat.interfaces;


import com.doubleclick.x_course.PyChat.models.Message;

import java.util.ArrayList;

public interface OnUserDetailFragmentInteraction {
    void getAttachments();

    ArrayList<Message> getAttachments(int tabPos);

    void switchToMediaFragment();
}
