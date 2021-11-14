package com.doubleclick.x_course.CustomNavigation;


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
import com.doubleclick.x_course.ViewModel.MainViewModel;
import com.doubleclick.x_course.ViewModel.MobileViewModel;
import com.doubleclick.x_course.ViewModel.WebViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;


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
//    private Toolbar toolbar;


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

//        toolbar = view.findViewById(R.id.toolbar);
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        activity.setSupportActionBar(toolbar);

        lifecycleOwner = getViewLifecycleOwner();
//        viewModelStoreOwner = (ViewModelStoreOwner) getViewLifecycleOwner();
        mobileViewModel = new ViewModelProvider(this).get(MobileViewModel.class);
        webViewModel = new ViewModelProvider(this).get(WebViewModel.class);
        graphicDesignViewModel = new ViewModelProvider(this).get(GraphicDesignViewModel.class);

        main_courses = view.findViewById(R.id.main_courses);
        recycler_Advertisement = view.findViewById(R.id.recycler_Advertisement);
        MainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recycler_Advertisement.setLayoutManager(linearLayoutManager);
        loadingAnimView = view.findViewById(R.id.loadingAnimView);
        if (mAuth != null && firebaseUser != null) {
            mainViewModel.getLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Diploma>>() {
                @Override
                public void onChanged(ArrayList<Diploma> diplomas) {
//                    LoadAllDiplomas(diplomas);
                    if (!diplomas.isEmpty()) {
                        Log.e("MainFragment  = ",""+diplomas.toString());
                        loadingAnimView.setVisibility(View.GONE);
                        LoadAllDiplomas(diplomas);
                    }
                }
            });

            mobileViewModel.getMobileData().observe(lifecycleOwner, new Observer<ArrayList<Diploma>>() {
                @Override
                public void onChanged(ArrayList<Diploma> diplomas) {
                    if (!diplomas.isEmpty()){
//                        LoadAllDiplomas(diplomas);
                    }
                }
            });

//            webViewModel.getWebData().observe(lifecycleOwner, new Observer<ArrayList<Diploma>>() {
//                @Override
//                public void onChanged(ArrayList<Diploma> diplomas) {
//                    if (!diplomas.isEmpty()){
//                        LoadAllDiplomas(diplomas);
//                    }
//                }
//            });

//            graphicDesignViewModel.getGraphicDesign().observe(lifecycleOwner, new Observer<ArrayList<Diploma>>() {
//                @Override
//                public void onChanged(ArrayList<Diploma> diplomas) {
//                    if (!diplomas.isEmpty()){
//                        LoadAllDiplomas(diplomas);
//                    }
//                }
//            });

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
        ArrayList<ItemCourse> itemCourses = new ArrayList<>();
        itemCourses.add(new ItemCourse(R.drawable.android,"Android",R.drawable.item_blue));
        itemCourses.add(new ItemCourse(R.drawable.web,"Web",R.drawable.item_blue));
        itemCourses.add(new ItemCourse(R.drawable.graphic_,"GraphicDesgin",R.drawable.item_blue));
        ItemCourseAdapter itemCourseAdapter = new ItemCourseAdapter(itemCourses);
        itemCourseAdapter.onClickItemListener(new ItemCourseAdapter.itemListener() {
            @Override
            public void mListener(int postion, ItemCourseAdapter.ItemCourseViewHolder holder) {
                if (holder.getAdapterPosition()==0){
//                    mobileViewModel = new ViewModelProvider(viewModelStoreOwner).get(MobileViewModel.class);
                    mobileViewModel.getMobileData().observe(lifecycleOwner, new Observer<ArrayList<Diploma>>() {
                        @Override
                        public void onChanged(ArrayList<Diploma> diplomas) {
                            Toast.makeText(getContext(), "" + diplomas.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }else if (holder.getAdapterPosition()==1){
//                    webViewModel = new ViewModelProvider(viewModelStoreOwner).get(WebViewModel.class);
                    webViewModel.getWebData().observe(lifecycleOwner, new Observer<ArrayList<Diploma>>() {
                        @Override
                        public void onChanged(ArrayList<Diploma> diplomas) {
                            Toast.makeText(getContext(), "" + diplomas.toString(), Toast.LENGTH_LONG).show();
//                            LoadAllDiplomas(diplomas);
                        }
                    });
                }else if (postion==2){
//                    graphicDesignViewModel = new ViewModelProvider(viewModelStoreOwner).get(GraphicDesignViewModel.class);
                    graphicDesignViewModel.getGraphicDesign().observe(lifecycleOwner, new Observer<ArrayList<Diploma>>() {
                        @Override
                        public void onChanged(ArrayList<Diploma> diplomas) {
                            Toast.makeText(getContext(), "" + diplomas.toString(), Toast.LENGTH_LONG).show();

                        }
                    });
                }

            }
        });
        main_courses.setAdapter(itemCourseAdapter);
        return view;
    }



    private void LoadAllDiplomas(ArrayList<Diploma> diplomas) {
//        homePageArrayList.add(new HomePage(diplomas,mEmail,mUserId,mUserName,mImageURL,1));
        diplomasAdapter = new DiplomasAdapter(diplomas, mEmail, mUserId, mUserName, mImageURL, getContext());
        MainRecyclerView.setAdapter(diplomasAdapter); //homePageAdapter
//        main_courses.setAdapter(diplomasAdapter);
        diplomasAdapter.notifyDataSetChanged();//homePageAdapter
    }

}