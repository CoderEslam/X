package com.doubleclick.x_course.Adapter;


import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

import android.annotation.SuppressLint;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.doubleclick.x_course.CourseActivity;
import com.doubleclick.x_course.Model.YouTubeDataModel;
import com.doubleclick.x_course.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;


public class VideosPostVarticalAdapter extends RecyclerView.Adapter<VideosPostVarticalAdapter.YouTubePostHolder> {


    public VideosPostVarticalAdapter(ArrayList<YouTubeDataModel> youTubeDataModels) {
        this.youTubeDataModels = youTubeDataModels;

    }
    private ArrayList<YouTubeDataModel> youTubeDataModels;



    @NonNull
    @Override
    public YouTubePostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.you_tube_post, parent, false);
        YouTubePostHolder youTubePostHolder = new YouTubePostHolder(view);
        return youTubePostHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull YouTubePostHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.Title.setText(youTubeDataModels.get(position).getTitle());
        holder.Date.setText(youTubeDataModels.get(position).getPublishedAt());
        holder.Des.setText(youTubeDataModels.get(position).getDescribtion());
        if (youTubeDataModels.get(position).getThumnail() != null) {
            holder.loading_indicator.setVisibility(View.GONE);
        } else {
            holder.loading_indicator.setVisibility(View.VISIBLE);
        }
        //TODO: image will be downloaded from url
        Picasso.with(holder.itemView.getContext()).load(youTubeDataModels.get(position).getThumnail()).placeholder(R.drawable.loading_icon).into(holder.ImageThumb);

    }



    @Override
    public int getItemCount() {
        try {
            return youTubeDataModels.size();
        }catch (NullPointerException e){
            return 0;
        }
    }


    public class YouTubePostHolder extends RecyclerView.ViewHolder {
        TextView Title;
        TextView Des;
        TextView Date;
        CircleImageView ImageThumb;
        ImageView playVideo;
        LottieAnimationView loading_indicator;

        public YouTubePostHolder(@NonNull View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.Title);
            Des = itemView.findViewById(R.id.Des);
            Date = itemView.findViewById(R.id.Date);
            ImageThumb = itemView.findViewById(R.id.ImageThumb);
            playVideo = itemView.findViewById(R.id.playVideo);
            loading_indicator = itemView.findViewById(R.id.loading_indicator);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CourseActivity courseActivity  = new CourseActivity();
                    courseActivity.finish();
                    Intent intent  = new Intent(itemView.getContext(),CourseActivity.class);
                    intent.putExtra("positionVideoFromVideosPostVarticalAdapter",getAdapterPosition()); //positionVideo
                    intent.putExtra("IdVideo",youTubeDataModels.get(getAdapterPosition()).getVideo_id());
                    intent.putExtra("TitelVideo",youTubeDataModels.get(getAdapterPosition()).getTitle());
                    intent.putExtra("DescribtionVideo",youTubeDataModels.get(getAdapterPosition()).getDescribtion());
                    intent.setFlags(FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }


}
