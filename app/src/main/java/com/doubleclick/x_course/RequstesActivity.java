package com.doubleclick.x_course;




import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.doubleclick.x_course.Adapter.RequestsAdapter;
import com.doubleclick.x_course.Model.Request;
import com.doubleclick.x_course.ViewModel.RequestViewModel;

import java.util.ArrayList;

public class RequstesActivity extends AppCompatActivity {

    private RecyclerView RequestsRecyclerView;
    private RequestViewModel requestViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requstes);
        RequestsRecyclerView  = findViewById(R.id.RequestsRecyclerView);
        RequestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestViewModel = new ViewModelProvider(this).get(RequestViewModel.class);
        requestViewModel.getLiveData().observe(this, new Observer<ArrayList<Request>>() {
            @Override
            public void onChanged(ArrayList<Request> requests) {
                RequestsAdapter requestsAdapter = new RequestsAdapter(requests);
                RequestsRecyclerView.setAdapter(requestsAdapter);
            }
        });

    }
}