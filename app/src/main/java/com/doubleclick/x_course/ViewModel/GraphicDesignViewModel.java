package com.doubleclick.x_course.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doubleclick.x_course.Model.Diploma;
import com.doubleclick.x_course.Repositorys.GraphicDesignReopsitory;

import java.util.ArrayList;

public class GraphicDesignViewModel extends ViewModel implements GraphicDesignReopsitory.GraphicDesignListener {


    MutableLiveData<ArrayList<Diploma>> mutableLiveData = new MutableLiveData<>();

    GraphicDesignReopsitory graphicDesignReopsitory = new GraphicDesignReopsitory(this);

    public GraphicDesignViewModel() {
        graphicDesignReopsitory.getGraphicDesignDate();
    }

    public LiveData<ArrayList<Diploma>> getGraphicDesign() {
        return mutableLiveData;
    }


    @Override
    public void loadGraphicDesignData(ArrayList<Diploma> diplomaArrayList) {
        mutableLiveData.setValue(diplomaArrayList);
    }
}
