package com.doubleclick.x_course.Adapter;


import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.doubleclick.x_course.Model.Advertisement;
import com.doubleclick.x_course.Model.Diploma;
import com.doubleclick.x_course.Model.HomePage;
import com.doubleclick.x_course.Model.YouTubeDataModel;
import com.doubleclick.x_course.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageAdapter extends RecyclerView.Adapter {

    private ArrayList<HomePage> homePageList = new ArrayList<>();


    public HomePageAdapter(ArrayList<HomePage> homePageList) {
        this.homePageList = homePageList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case HomePage.Advertisement://0
                View bannerSliderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.slids_ad_banner, parent, false);
                return new BannerSliderViewholder(bannerSliderView);
            case HomePage.AllDiploma://1
                View item_diplomaView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_diploma, parent, false);
                return new DiplomaViewHolder(item_diplomaView);
            case HomePage.ItemCourse://2
                View item_VideoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_video, parent, false);
                return new ItemVideoViewHolder(item_VideoView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (homePageList.get(position).getType()) {
            case HomePage.Advertisement://0
                List<Advertisement> advertisements = homePageList.get(position).getAdvertisementArrayList();
                ((BannerSliderViewholder) holder).setBannerSliderViewPager(advertisements);
                break;

            case HomePage.AllDiploma://1
                ArrayList<Diploma> AllDiploma = homePageList.get(position).getDiplomaArrayList();
                String name = homePageList.get(position).getUserName();
                String id = homePageList.get(position).getUserId();
                String image = homePageList.get(position).getImageURL();
                String email = homePageList.get(position).getEmail();
                ((DiplomaViewHolder) holder).setDiploma(AllDiploma, name, id, image, email);
                break;

            case HomePage.ItemCourse://2
                ArrayList<YouTubeDataModel> youTubeDataModels = homePageList.get(position).getYouTubeDataModelList();
                Toast.makeText(holder.itemView.getContext(), "Done  = ", Toast.LENGTH_SHORT).show();
                ((ItemVideoViewHolder) holder).setVideo(youTubeDataModels);
                break;

        }

    }

    @Override
    public int getItemCount() {
        return homePageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageList.get(position).getType()) {
            case 0:
                return HomePage.Advertisement;//0
            case 1:
                return HomePage.AllDiploma;//1
            case 2:
                return HomePage.ItemCourse;//2
            default:
                return -1;
        }
    }


    public class DiplomaViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView h_s_Recycler_Scroll;

        public DiplomaViewHolder(@NonNull View itemView) {
            super(itemView);
            h_s_Recycler_Scroll = itemView.findViewById(R.id.h_s_Recycler_Scroll);
        }

        public void setDiploma(ArrayList<Diploma> allDiploma, String name, String id, String image, String email) {
            DiplomasAdapter diplomasAdapter = new DiplomasAdapter(allDiploma, email, id, name, image, itemView.getContext());
            h_s_Recycler_Scroll.setAdapter(diplomasAdapter);
        }


    }

    public class ItemVideoViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView Rec_All_Video;
        public ItemVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            Rec_All_Video = itemView.findViewById(R.id.Rec_All_Video);
        }

        public void setVideo(ArrayList<YouTubeDataModel> allVideo) {
            VideosPostVarticalAdapter videosPostVarticalAdapter = new VideosPostVarticalAdapter(allVideo);
            Rec_All_Video.setAdapter(videosPostVarticalAdapter);
            videosPostVarticalAdapter.notifyDataSetChanged();
        }

    }

    public class BannerSliderViewholder extends RecyclerView.ViewHolder {  //0

        private ViewPager bannerSliderViewPager;
        private int currentPage;
        private Timer timer;
        private final long DELAY_TIME = 2000;
        private final long PERIOD_TIME = 2000;

        public BannerSliderViewholder(@NonNull View itemView) {
            super(itemView);
            bannerSliderViewPager = itemView.findViewById(R.id.banner_slier_view_pager);
        }

        private void setBannerSliderViewPager(final List<Advertisement> advertisements) {
            currentPage = 2;
//////////////////////////////////////////////////////////////////
            SliderAdapter sliderAdapter = new SliderAdapter(advertisements);
            bannerSliderViewPager.setAdapter(sliderAdapter);
            bannerSliderViewPager.setClipToPadding(false);
            bannerSliderViewPager.setPageMargin(20);
            bannerSliderViewPager.setCurrentItem(currentPage);

            ///////////////////////////////////////////////////////////////////////////////////////////////////////
            //     onPageChangeListener
            bannerSliderViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { //is not important
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
//                        PageLooper(sliderModelList);  // is not important
                    }
                }
            });
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////
            StartbannerSlideShow(advertisements);
            //if banner Touch this mathod is excut
            bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    StopBannerSlideShow();
                    //
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        StartbannerSlideShow(advertisements);
                    }
                    return false;
                }
            });
        }


        // this resbonsable to loop slider
        private void StartbannerSlideShow(final List<Advertisement> advertisementList) {
            try {
                final Handler handler = new Handler();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        if (currentPage >= advertisementList.size()) {
                            currentPage = 0;
                        }

                        bannerSliderViewPager.setCurrentItem(currentPage++);
                    }
                };
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(runnable);
                    }
                }, DELAY_TIME, PERIOD_TIME);
            } catch (NullPointerException e) {
                Log.e("HomePagerAdapter : ", e.getMessage());
            }

        }

        private void StopBannerSlideShow() {
            timer.cancel();
        }

    }

}
