package com.doubleclick.x_course;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.doubleclick.x_course.Model.YouTubeDataModel;
import com.doubleclick.x_course.ViewModel.LoadYouTubeViewModel;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.Collections;

public class AboutCourseActivity extends AppCompatActivity {


    private TextView tv_nameOfDiploma,
            tv_nameOfDevelpoer,
            tv_aboutCourses,
            tv_lastPrice,
            tv_newPrice;
    private ImageButton seeAllVideo;
    private YouTubePlayerView youTubePlayerView;
    private LottieAnimationView animationDiscount;
    private String name_Diplomat,
            name_Developer,
            last_Price,
            new_Price,
            url_animation,
            about_course,
            numberOfDiploma,
            promo_youtube = "",
            email, keyJoin,
            timestamp;
    private boolean check;
    private static String GOOGLE_YOUTUBE_API_KEY = "AIzaSyB-c6ay-9BkRmdltEFr-zpSjNj6XPmvNuc";//here you should use your api key for testing purpose you can use this api also
    private LoadYouTubeViewModel loadYouTubeViewModel;
    public static ArrayList<ArrayList<YouTubeDataModel>> AllYouTubeArrayLists = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_course);
        tv_nameOfDiploma = findViewById(R.id.nameOfDiploma);
        tv_nameOfDevelpoer = findViewById(R.id.nameOfDevelpoer);
        tv_lastPrice = findViewById(R.id.lastPrice);
        tv_aboutCourses = findViewById(R.id.aboutCourses);
        tv_newPrice = findViewById(R.id.newPrice);
        seeAllVideo = findViewById(R.id.seeAllVideo);
        youTubePlayerView = findViewById(R.id.promo_youtube);
        animationDiscount = findViewById(R.id.animationDiscount);
        try {
            name_Diplomat = getIntent().getStringExtra("nameOfDiploma");
            name_Developer = getIntent().getStringExtra("nameOfDevelpoer");
            last_Price = getIntent().getStringExtra("lastPrice");
            about_course = getIntent().getStringExtra("aboutCourses");
            new_Price = getIntent().getStringExtra("newPrice");
            timestamp = getIntent().getStringExtra("timestamp");
            url_animation = getIntent().getStringExtra("animationDiscount");
            numberOfDiploma = getIntent().getStringExtra("numberOfDiploma");
            promo_youtube = getIntent().getStringExtra("promo_youtube");
            email = getIntent().getStringExtra("email");
            check = getIntent().getBooleanExtra("check", false);
            keyJoin = getIntent().getStringExtra("keyJoin");
            getLifecycle().addObserver(youTubePlayerView);
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    if (promo_youtube == null) {
                        youTubePlayer.loadVideo("poye96xY10w", 0);
                    } else {
                        youTubePlayer.loadVideo(promo_youtube, 0);
                    }
                }
            });
            if (check && keyJoin.equals("Watch")) {
                loadYouTubeViewModel = new ViewModelProvider(this).get(LoadYouTubeViewModel.class);
                loadYouTubeViewModel.Getter(name_Diplomat, numberOfDiploma, email,name_Developer,timestamp);
                loadYouTubeViewModel.getData().observe(this, new Observer<ArrayList<YouTubeDataModel>>() {
                    @Override
                    public void onChanged(ArrayList<YouTubeDataModel> youTubeDataModels) {
                        if (youTubeDataModels.size() != 0) {
                            Log.e("AboutCourseActivity = ", youTubeDataModels.toString());
                            AllYouTubeArrayLists.add(youTubeDataModels);
                            Intent intent = new Intent(AboutCourseActivity.this, CourseActivity.class);
                            startActivity(intent);
                        }

                    }
                });

            }
            tv_nameOfDiploma.setText(name_Diplomat + " " + numberOfDiploma);
            tv_nameOfDevelpoer.setText(name_Developer);
            tv_aboutCourses.setText(about_course);
            try {
                int descount = (Integer.parseInt(last_Price) - Integer.parseInt(new_Price));
                if (descount == 0) {
                    tv_lastPrice.setText(last_Price);
                    tv_newPrice.setText(new_Price);
                    tv_lastPrice.setVisibility(View.GONE);
                    animationDiscount.setVisibility(View.GONE);
                } else {
                    tv_lastPrice.setText(last_Price);
                    tv_newPrice.setText(new_Price);
                    try {
                        if (url_animation.equals("")) {
                            animationDiscount.setVisibility(View.GONE);
                        } else {
                            animationDiscount.setVisibility(View.VISIBLE);
                            animationDiscount.setAnimationFromUrl(url_animation);
                        }
                    }catch (NullPointerException e){

                    }

                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        seeAllVideo.setOnClickListener(view -> {
            if (check && keyJoin.equals("Watch")) {
                if (AllYouTubeArrayLists.size() != 0) {
                    Intent intent = new Intent(AboutCourseActivity.this, CourseActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AboutCourseActivity.this, "Wait to loading....", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(AboutCourseActivity.this, "Can't , subscrip first", Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AboutCourseActivity.this, NavigationDrawerActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
    }
}