package com.doubleclick.x_course.Adapter;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.doubleclick.x_course.Model.Advertisement;
import com.doubleclick.x_course.R;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private List<Advertisement> sliderModelList;
    private AlertDialog.Builder builder;

    public SliderAdapter(List<Advertisement> advertisementList) {
        this.sliderModelList = advertisementList;
    }


    @Override
    public int getCount() {
        return sliderModelList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.slider_image_layout, container, false);
        ImageView banner = view.findViewById(R.id.banner_sliderImageView);
        builder = new AlertDialog.Builder(container.getContext());
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (sliderModelList.get(position).getVideoId() != null && !sliderModelList.get(position).getVideoId().equals("")) {
                        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + sliderModelList.get(position).getVideoId()));
                        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + sliderModelList.get(position).getVideoId()));
                        try {
                            container.getContext().startActivity(appIntent);
                        } catch (ActivityNotFoundException ex) {
                            container.getContext().startActivity(webIntent);
                        }
                    } else if (sliderModelList.get(position).getAbout() != null && !sliderModelList.get(position).getAbout().equals("")) {
                        View viewEditCart = LayoutInflater.from(container.getContext()).inflate(R.layout.item_about, null, false);
                        ImageView imageView = viewEditCart.findViewById(R.id.image);
                        TextView txt = viewEditCart.findViewById(R.id.about);
                        Glide.with(container.getContext()).load(sliderModelList.get(position).getImage()).placeholder(R.drawable.loading_icon).into(imageView);
                        txt.setText(sliderModelList.get(position).getAbout());
                        builder.setTitle("About");
                        builder.setView(viewEditCart);
                        builder.show();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        });
        Glide.with(view).load(sliderModelList.get(position).getImage()).into(banner);
//        Picasso.get().load(sliderModelList.get(position).getImageAd()).placeholder(R.drawable.parson).into(banner);
        container.addView(view, 0);
        return view;
    }
}
