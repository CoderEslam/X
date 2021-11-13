package com.doubleclick.x_course;


import static com.doubleclick.x_course.AboutCourseActivity.AllYouTubeArrayLists;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.doubleclick.x_course.Adapter.HomePageAdapter;
import com.doubleclick.x_course.FolatingLayout.FloatingLayout;
import com.doubleclick.x_course.FolatingLayout.callback.FloatingListener;
import com.doubleclick.x_course.Model.Emails;
import com.doubleclick.x_course.Model.HomePage;
import com.doubleclick.x_course.Model.YouTubeDataModel;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;


import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;


import java.util.ArrayList;

public class CourseActivity extends YouTubeBaseActivity {

    private TextView nameOfLectuer, Description;
    private static RecyclerView allVideos;
    private ArrayList<Emails> emails = new ArrayList<>();
    private String GOOGLE_YOUTUBE_API_KEY = "AIzaSyB-c6ay-9BkRmdltEFr-zpSjNj6XPmvNuc";//here you should use your api key for testing purpose you can use this api also
    private HomePageAdapter homePageAdapter;
    private ArrayList<HomePage> homePages = new ArrayList<>();
    private ArrayList<YouTubeDataModel> mListData = new ArrayList<>();
    private LottieAnimationView loadingAnimView;
    private FloatingLayout floatingLayout;
    private com.google.android.youtube.player.YouTubePlayerView Lectuer;
    private com.google.android.youtube.player.YouTubePlayer mYouTubePlayer;
    private int sec;
    private int positionVideoVertical, positionVideoFromVideosHomePageAdapter;
    private String IdVideo, TitelVideo, DescribtionVideo;
//    public static ArrayList<ArrayList<YouTubeDataModel>> AllGivenYouTubeArrayLists = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        nameOfLectuer = findViewById(R.id.nameOfLectuer);
        Description = findViewById(R.id.Description);
        allVideos = findViewById(R.id.allVideos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        allVideos.setLayoutManager(linearLayoutManager);
        Lectuer = findViewById(R.id.Lectuer);
        loadingAnimView = findViewById(R.id.loadingAnimView);
        try {
            for (int i = 0; i < AllYouTubeArrayLists.size(); i++) {
                initList(AllYouTubeArrayLists.get(i));
            }
            positionVideoVertical = getIntent().getIntExtra("positionVideoFromVideosPostVarticalAdapter", 0);
            IdVideo = getIntent().getStringExtra("IdVideo");
            TitelVideo = getIntent().getStringExtra("TitelVideo");
            DescribtionVideo = getIntent().getStringExtra("DescribtionVideo");
            if (DescribtionVideo == null && TitelVideo == null) {
                try {
                    nameOfLectuer.setText(AllYouTubeArrayLists.get(0).get(0).getTitle());
                    Description.setText(AllYouTubeArrayLists.get(0).get(0).getDescribtion());
                } catch (IndexOutOfBoundsException e) {
                    nameOfLectuer.setText("");
                    Description.setText("");
                    Log.e("Course Activity at 85", "error  = " + e.toString());
                }

            } else {
                nameOfLectuer.setText(TitelVideo);
                Description.setText(DescribtionVideo);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (isNeedPermission()) {
            requestPermission();
        }

        Lectuer.initialize(GOOGLE_YOUTUBE_API_KEY, new com.google.android.youtube.player.YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(com.google.android.youtube.player.YouTubePlayer.Provider provider, com.google.android.youtube.player.YouTubePlayer youTubePlayer, boolean b) {

                mYouTubePlayer = youTubePlayer;
                if (sec == 0) {
                    if (IdVideo == null) {
                        try {
                            youTubePlayer.loadVideo(AllYouTubeArrayLists.get(0).get(0).getVideo_id(), 0);
                        } catch (IndexOutOfBoundsException e) {

                        }
                    } else {
                        youTubePlayer.loadVideo(IdVideo, 0);
                    }
                } else {
                    youTubePlayer.loadVideo(IdVideo, sec);
                }
            }

            @Override
            public void onInitializationFailure(com.google.android.youtube.player.YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            SharedPreferences.Editor editor = getSharedPreferences("VideoSecond", MODE_PRIVATE).edit();
            editor.putFloat("second", mYouTubePlayer.getCurrentTimeMillis());
            editor.apply();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private final FloatingListener floatingListener = new FloatingListener() {
        @Override
        public void onCreateListener(View view) {
            ImageView btn = view.findViewById(R.id.btn_close);
            YouTubePlayerView youtube_player_view = view.findViewById(R.id.youtube_player_view);
            youtube_player_view.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    SharedPreferences preferences = getSharedPreferences("VideoSecond", MODE_PRIVATE);
                    float second = preferences.getFloat("second", 0);
                    if (second == 0 && IdVideo == null) {
                        try {
                            youTubePlayer.loadVideo(AllYouTubeArrayLists.get(0).get(0).getVideo_id(), 0);
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    } else if (second != 0 && IdVideo == null) {
                        try {
                            youTubePlayer.loadVideo(AllYouTubeArrayLists.get(0).get(0).getVideo_id(), (second / 1000f));
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    } else if (second != 0 && IdVideo != null) {
                        try {
                            youTubePlayer.loadVideo(IdVideo, (second / 1000f));
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }

                    }

                }
            });

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    youtube_player_view.release();// to stop playing when click exit in  backend
                    floatingLayout.destroy();
                }
            });
        }

        @Override
        public void onCloseListener() {
            Toast.makeText(getApplicationContext(), "Closed", Toast.LENGTH_SHORT).show();
        }
    };

    public void initList(ArrayList<YouTubeDataModel> mListData) {
        if (mListData != null) {
            loadingAnimView.setVisibility(View.GONE);
            // ToDo if ()nameofdiploma==web{..............}
            homePages.add(new HomePage(0, mListData));
            homePageAdapter = new HomePageAdapter(homePages);
            allVideos.setAdapter(homePageAdapter);
            homePageAdapter.notifyDataSetChanged();
        }
    }

    private boolean isNeedPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this);
    }

    private void requestPermission() {
        Intent intent = new Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName())
        );
        startActivityForResult(intent, 25);
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        try {
            bundle.putInt("sec", mYouTubePlayer.getCurrentTimeMillis());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            if (savedInstanceState.getInt("sec") != 0) {
                sec = savedInstanceState.getInt("sec");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (IdVideo != null) {
            if (!isNeedPermission()) {
                showFloating();
            }
        }
        AllYouTubeArrayLists.clear(); //  to clear all data after out of the activity
        Intent intent = new Intent(CourseActivity.this, NavigationDrawerActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
    }


    private void showFloating() {
        floatingLayout = new FloatingLayout(this, R.layout.floating_layout);
        floatingLayout.setFloatingListener(floatingListener);
        floatingLayout.create();
    }

}