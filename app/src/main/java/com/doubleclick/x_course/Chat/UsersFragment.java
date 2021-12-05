package com.doubleclick.x_course.Chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.doubleclick.x_course.Adapter.UserAdapter;
import com.doubleclick.x_course.PyChat.models.User;
import com.doubleclick.x_course.R;
import com.doubleclick.x_course.ViewModel.AllUsersViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<User> mUsers;
    private FirebaseAuth mAuth;
    private String UserID;
    EditText search_users;
    private AllUsersViewModel allUsersViewModel;


    public UsersFragment() {
        // Required empty public constructor
    }

    public static UsersFragment newInstance(String param1, String param2) {
        UsersFragment fragment = new UsersFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        allUsersViewModel = new ViewModelProvider(this).get(AllUsersViewModel.class);
        recyclerView = view.findViewById(R.id.recycler_view_User);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth = FirebaseAuth.getInstance();
        mUsers = new ArrayList<>();
        UserID = mAuth.getCurrentUser().getUid().toString();
        allUsersViewModel.getAllUsersLiveData().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                userAdapter = new UserAdapter(getContext(), users, false);
                recyclerView.setAdapter(userAdapter);
            }
        });

        return view;
    }

}