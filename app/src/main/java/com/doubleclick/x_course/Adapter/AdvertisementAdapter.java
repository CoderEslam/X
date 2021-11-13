package com.doubleclick.x_course.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.doubleclick.x_course.Model.Advertisement;
import com.doubleclick.x_course.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdvertisementAdapter extends RecyclerView.Adapter<AdvertisementAdapter.AdvertisementViewHolder> {
    private ArrayList<Advertisement> advertisements = new ArrayList<>();

    public AdvertisementAdapter(ArrayList<Advertisement> advertisements) {
        this.advertisements = advertisements;
    }

    @NonNull
    @Override
    public AdvertisementAdapter.AdvertisementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_advertisement,parent,false);
        return new AdvertisementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvertisementAdapter.AdvertisementViewHolder holder, int position) {
        holder.aboutDiploma.setText(advertisements.get(holder.getAdapterPosition()).getAbout());
        Glide.with(holder.itemView.getContext()).load(advertisements.get(holder.getAdapterPosition()).getImage()).placeholder(R.drawable.loading_icon).into(holder.image_diploma);

    }

    public Advertisement getAdv(int pos){
        return advertisements.get(pos);
    }

    @Override
    public int getItemCount() {
        return advertisements.size();
    }

    public class AdvertisementViewHolder extends RecyclerView.ViewHolder {
        private TextView aboutDiploma;
        private CircleImageView image_diploma;
        public AdvertisementViewHolder(@NonNull View itemView) {
            super(itemView);
            aboutDiploma = itemView.findViewById(R.id.aboutDiploma);
            image_diploma = itemView.findViewById(R.id.image_diploma);
        }
    }
}
