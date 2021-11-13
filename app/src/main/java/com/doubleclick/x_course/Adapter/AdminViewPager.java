package com.doubleclick.x_course.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.doubleclick.x_course.Admin.BindingPlayListFragment;
import com.doubleclick.x_course.Admin.PutEmailFragment;

public class AdminViewPager extends ViewPagerAdapter {

    private int nNumOfTabs;

    public AdminViewPager(@NonNull FragmentManager fm, int NumOfTaps) {
        super(fm, NumOfTaps);
        this.nNumOfTabs = NumOfTaps;


    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                PutEmailFragment putEmailFragment = new PutEmailFragment();
                return putEmailFragment;
            case 1:
                BindingPlayListFragment createDiplomaFragment = new BindingPlayListFragment();
                return createDiplomaFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return nNumOfTabs;
    }
}
