package com.doubleclick.x_course.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.doubleclick.x_course.Chat.ChatFragment;
import com.doubleclick.x_course.Chat.UsersFragment;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private int nNumOfTabs;
    public ViewPagerAdapter(@NonNull FragmentManager fm, int NumOfTaps) {
        super(fm);
        this.nNumOfTabs = NumOfTaps;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;
            case 1:
                UsersFragment usersFragment = new UsersFragment();
                return usersFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return nNumOfTabs;
    }
}
