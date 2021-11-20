package com.doubleclick.x_course.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doubleclick.x_course.Model.ItemCourse;
import com.doubleclick.x_course.Repositorys.ItemCourseRepository;

import java.util.ArrayList;


public class ItemViewModel extends ViewModel implements ItemCourseRepository.ItemListener {


    ItemCourseRepository itemCourseRepository = new ItemCourseRepository(this);
    MutableLiveData<ArrayList<ItemCourse>> itemCourseMutableLiveData = new MutableLiveData<>();

    public ItemViewModel() {
        itemCourseRepository.getItems();
    }
    public LiveData<ArrayList<ItemCourse>> getItemCourse(){
        return itemCourseMutableLiveData;
    }

    @Override
    public void mListener(ArrayList<ItemCourse> itemCourse) {
        itemCourseMutableLiveData.setValue(itemCourse);
    }
}
