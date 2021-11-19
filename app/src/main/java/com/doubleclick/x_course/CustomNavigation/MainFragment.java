package com.doubleclick.x_course.CustomNavigation;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.doubleclick.x_course.Adapter.DiplomasAdapter;
import com.doubleclick.x_course.Adapter.HomePageAdapter;
import com.doubleclick.x_course.Adapter.ItemCourseAdapter;
import com.doubleclick.x_course.Model.Advertisement;
import com.doubleclick.x_course.Model.Diploma;
import com.doubleclick.x_course.Model.HomePage;
import com.doubleclick.x_course.Model.ItemCourse;
import com.doubleclick.x_course.NavigationDrawerActivity;
import com.doubleclick.x_course.R;
import com.doubleclick.x_course.ViewModel.AdvertisementViewModel;
import com.doubleclick.x_course.ViewModel.GraphicDesignViewModel;
import com.doubleclick.x_course.ViewModel.ItemViewModel;
import com.doubleclick.x_course.ViewModel.MainViewModel;
import com.doubleclick.x_course.ViewModel.MobileViewModel;
import com.doubleclick.x_course.ViewModel.WebViewModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment {


    private static final String Email = "Email";
    private static String mEmail;
    private static String mUserId;
    private static String mUserName;
    private static String mImageURL;
    private RecyclerView /* MainRecyclerView,*/ recycler_Advertisement;
    private ShimmerRecyclerView MainRecyclerView;
    private LottieAnimationView loadingAnimView;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private static View view;
    private AdvertisementViewModel advertisementViewModel;
    private static String GOOGLE_YOUTUBE_API_KEY = "AIzaSyB-c6ay-9BkRmdltEFr-zpSjNj6XPmvNuc";//here you should use your api key for testing purpose you can use this api also
    private HomePageAdapter homePageAdapter;
    private ArrayList<HomePage> homePageArrayList = new ArrayList<>();
    DiplomasAdapter diplomasAdapter;
    private MainViewModel mainViewModel;
    private MobileViewModel mobileViewModel;
    private WebViewModel webViewModel;
    private GraphicDesignViewModel graphicDesignViewModel;
    private ShimmerRecyclerView main_courses;
    LifecycleOwner lifecycleOwner;
    ViewModelStoreOwner viewModelStoreOwner;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    private ItemViewModel itemViewModel;
//    private String names[] = {"Android", "Web", "GraphicDesgin"};
//    private int icons[] = {R.drawable.android, R.drawable.web, R.drawable.graphic_};
    ArrayList<ItemCourse> itemCourses = new ArrayList<>();
    private ItemCourseAdapter itemCourseAdapter;
    public MainFragment() {
        // Required empty public constructor
    }


    public static MainFragment newInstance(String email, String UserId, String userName, String imageURL) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        mEmail = email;
        mUserId = UserId;
        mUserName = userName;
        mImageURL = imageURL;
        args.putString(Email, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        setHasOptionsMenu(true);
        connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (mAuth != null && firebaseUser != null) {
            mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
            advertisementViewModel = new ViewModelProvider(this).get(AdvertisementViewModel.class);
            homePageAdapter = new HomePageAdapter(homePageArrayList);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        MainRecyclerView = view.findViewById(R.id.MainRecyclerView);


        lifecycleOwner = getViewLifecycleOwner();
        viewModelStoreOwner = (ViewModelStoreOwner) getViewLifecycleOwner();
        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        itemViewModel.getItemCourse().observe(getViewLifecycleOwner(), new Observer<ArrayList<ItemCourse>>() {
            @Override
            public void onChanged(ArrayList<ItemCourse> itemCourses) {
                itemCourseAdapter = new ItemCourseAdapter(itemCourses);
                itemCourseAdapter.onClickItemListener(new ItemCourseAdapter.itemListener() {
                    @Override
                    public void mListener(int postion) {
                        if (postion == 0 && networkInfo != null && networkInfo.isConnected()) {
                            mobileViewModel = new ViewModelProvider(viewModelStoreOwner).get(MobileViewModel.class);
                            mobileViewModel.getMobileData().observe(lifecycleOwner, new Observer<ArrayList<Diploma>>() {
                                @Override
                                public void onChanged(ArrayList<Diploma> diplomas) {
                                    if (diplomas.size() != 0) {
                                        loadingAnimView.setVisibility(View.GONE);
                                        LoadAllDiplomas(diplomas);
                                    }
                                }
                            });
                        } else if (postion == 1 && networkInfo != null && networkInfo.isConnected()) {
                            webViewModel = new ViewModelProvider(viewModelStoreOwner).get(WebViewModel.class);
                            webViewModel.getWebData().observe(lifecycleOwner, new Observer<ArrayList<Diploma>>() {
                                @Override
                                public void onChanged(ArrayList<Diploma> diplomas) {
                                    if (networkInfo != null && networkInfo.isConnected() && diplomas.size() != 0) {
                                        LoadAllDiplomas(diplomas);
                                        loadingAnimView.setVisibility(View.GONE);
                                    } else {
                                        webViewModel.getWebData().removeObservers(lifecycleOwner);
                                        MainRecyclerView.setVisibility(View.GONE);

                                    }

                                }
                            });
                        } else if (postion == 2 && networkInfo != null && networkInfo.isConnected()) {
                            graphicDesignViewModel = new ViewModelProvider(viewModelStoreOwner).get(GraphicDesignViewModel.class);
                            graphicDesignViewModel.getGraphicDesign().observe(lifecycleOwner, new Observer<ArrayList<Diploma>>() {
                                @Override
                                public void onChanged(ArrayList<Diploma> diplomas) {
                                    if (diplomas.size() != 0) {
                                        loadingAnimView.setVisibility(View.GONE);
                                        LoadAllDiplomas(diplomas);
                                    }
                                }
                            });
                        }

                    }
                });
                main_courses.setAdapter(itemCourseAdapter);
            }
        });
        main_courses = view.findViewById(R.id.main_courses);
        recycler_Advertisement = view.findViewById(R.id.recycler_Advertisement);
        MainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recycler_Advertisement.setLayoutManager(linearLayoutManager);
        loadingAnimView = view.findViewById(R.id.loadingAnimView);
        if (mAuth != null && firebaseUser != null && networkInfo != null && networkInfo.isConnected()) {
            mainViewModel.getLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Diploma>>() {
                @Override
                public void onChanged(ArrayList<Diploma> diplomas) {
//                    LoadAllDiplomas(diplomas);
                    if (!diplomas.isEmpty()) {
                        loadingAnimView.setVisibility(View.GONE);
                        LoadAllDiplomas(diplomas);
                    }
                }
            });

            advertisementViewModel.LoadAdv().observe(getViewLifecycleOwner(), new Observer<ArrayList<Advertisement>>() {
                @Override
                public void onChanged(ArrayList<Advertisement> advertisements) {
                    if (advertisements.size() != 0) {
                        recycler_Advertisement.setVisibility(View.VISIBLE);
                        homePageArrayList.add(new HomePage(advertisements, 0));
                        recycler_Advertisement.setAdapter(homePageAdapter);
                        loadingAnimView.setVisibility(View.GONE);
                    } else {
                        recycler_Advertisement.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            loadingAnimView.setVisibility(View.GONE);
        }

        return view;
    }


    private void LoadAllDiplomas(ArrayList<Diploma> diplomas) {
        try {
//        homePageArrayList.add(new HomePage(diplomas,mEmail,mUserId,mUserName,mImageURL,1));
            diplomasAdapter = new DiplomasAdapter(diplomas, mEmail, mUserId, mUserName, mImageURL, getContext());
            MainRecyclerView.setAdapter(diplomasAdapter); //homePageAdapter
//        main_courses.setAdapter(diplomasAdapter);
            diplomasAdapter.notifyDataSetChanged();//homePageAdapter
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

}