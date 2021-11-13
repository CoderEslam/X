package com.doubleclick.x_course.Adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doubleclick.x_course.Model.ItemCourse;
import com.doubleclick.x_course.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemCourseAdapter extends RecyclerView.Adapter<ItemCourseAdapter.ItemCourseViewHolder> {

    private ArrayList<ItemCourse> itemCourses = new ArrayList<>();
    private boolean click = false;
    private int last_pos = -1;
    itemListener itemListener;

    public void onClickItemListener(itemListener itemListener){
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
        holder.icon.setImageResource(itemCourses.get(holder.getAdapterPosition()).getImage_());
        holder.courseName.setText(itemCourses.get(holder.getAdapterPosition()).getItemCourse());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                /*if (last_pos==holder.getAdapterPosition()){
                    // no thing todo
                }else if (last_pos!=holder.getAdapterPosition()){
                    holder.courseName.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.item_green));
                    if (last_pos!=-1){
                        holder.courseName.setBackgroundColor(itemCourses.get(last_pos).getBd_item());
                    }
                    last_pos = holder.getAdapterPosition();
                }*/
                itemListener.mListener(holder.getAdapterPosition(),holder);
            }
        });
    }

    public interface itemListener{
        void mListener(int postion,ItemCourseViewHolder holder);
    }

    @Override
    public int getItemCount() {
        return itemCourses.size();
    }

    public class ItemCourseViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView icon;
        public TextView courseName;
        public ItemCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.nameCourse);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}
