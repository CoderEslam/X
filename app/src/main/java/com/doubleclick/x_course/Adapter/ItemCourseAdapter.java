package com.doubleclick.x_course.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.doubleclick.x_course.Model.ItemCourse;
import com.doubleclick.x_course.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;

public class ItemCourseAdapter extends RecyclerView.Adapter<ItemCourseAdapter.ItemCourseViewHolder> {

    private ArrayList<ItemCourse> itemCourses = new ArrayList<>();
    private int lastCheckedPosition = -1;
    itemListener itemListener;

    public void onClickItemListener(itemListener itemListener) {
        this.itemListener = itemListener;
    }

    public ItemCourseAdapter(ArrayList<ItemCourse> itemCourses) {
        this.itemCourses = itemCourses;
    }

    @NonNull
    @Override
    public ItemCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new ItemCourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemCourseViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(itemCourses.get(holder.getAdapterPosition()).getImageDB()).into(holder.icon);
//        holder.icon.setImageResource(itemCourses.get(holder.getAdapterPosition()).getImage_());
        holder.courseName.setText(itemCourses.get(holder.getAdapterPosition()).getItemCourse());
        ItemCourse itemCourse = itemCourses.get(position);
        initializeViews(itemCourse, holder, position);

    }


    private void initializeViews(final ItemCourse model, final RecyclerView.ViewHolder holder, int position) {
        ((ItemCourseViewHolder) holder).courseName.setText(model.getItemCourse());
        if (model.getId() == lastCheckedPosition) {
            Drawable img = holder.itemView.getContext().getResources().getDrawable(R.drawable.cancel);
            img.setBounds(0, 0, 24, 24);
            ((ItemCourseViewHolder) holder).courseName.setCompoundDrawables(img, null, null, null);
            ((ItemCourseViewHolder) holder).courseName.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));
            ((ItemCourseViewHolder) holder).courseName.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.item_white));

        } else {
            Drawable img = holder.itemView.getContext().getResources().getDrawable(R.drawable.cancel);
            img.setBounds(0, 0, 0, 0);
            ((ItemCourseViewHolder) holder).courseName.setCompoundDrawables(null, null, null, null);
            ((ItemCourseViewHolder) holder).courseName.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.item_blue));

//            ((ItemCourseViewHolder) holder).check.setVisibility(View.GONE);
        }
        ((ItemCourseViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemListener.mListener(model.getId());
                lastCheckedPosition = model.getId();
                notifyItemRangeChanged(0, itemCourses.size());
            }
        });
    }


    public interface itemListener {
        void mListener(int postion);
    }

    @Override
    public int getItemCount() {
        return itemCourses.size();
    }

    public class ItemCourseViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView icon;
        public TextView courseName;
        private ImageView check;


        public ItemCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.nameCourse);
            icon = itemView.findViewById(R.id.icon);
            check = itemView.findViewById(R.id.check);
        }
    }
}
