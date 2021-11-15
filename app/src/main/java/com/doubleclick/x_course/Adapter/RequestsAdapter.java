package com.doubleclick.x_course.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.doubleclick.x_course.Model.Request;
import com.doubleclick.x_course.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestsViewHolder> {

    private ArrayList<Request> requestArrayList = new ArrayList<>();
    private DatabaseReference RequestReference;
    private DatabaseReference EmailsReference;
    private FirebaseAuth mAuth;
    private String UserId;


    public RequestsAdapter(ArrayList<Request> requestArrayList) {
        this.requestArrayList = requestArrayList;
        RequestReference = FirebaseDatabase.getInstance().getReference().child("Requests");
        EmailsReference = FirebaseDatabase.getInstance().getReference().child("Emails");
        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getUid().toString();
    }

    @NonNull
    @Override
    public RequestsAdapter.RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new RequestsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestsAdapter.RequestsViewHolder holder, int position) {
        Request request = requestArrayList.get(position);
        Glide.with(holder.itemView.getContext()).load(request.getImageURL()).placeholder(R.drawable.account_circle_24).into(holder.RequestImage);
        holder.RequestName.setText(request.getUsername());
        holder.TrackRequest.setText(request.getTrack());
        holder.animationAccept.setOnClickListener(view -> {
            Map mapEmail = new HashMap();
            String PushId = request.getUserId()+":"+request.getNameOfDiploma()+":"+request.getTrack()+":"+request.getNumberOfDiploma();
            String DiplomaId = request.getNameOfDevelpoer()+":"+request.getNameOfDiploma()+":"+request.getNumberOfDiploma()+":"+request.getTimestamp();
            mapEmail.put("diploma", request.getNameOfDiploma());
            mapEmail.put("numberOfDiploma", request.getNumberOfDiploma());
            mapEmail.put("track", request.getTrack()); // spinner
            mapEmail.put("PushId", PushId);
            mapEmail.put("UserId",UserId);
            mapEmail.put("DiplomaId",DiplomaId);
            EmailsReference.child(PushId).setValue(mapEmail);
            RequestReference.child(request.getPushId()).removeValue();
            holder.animationReject.setVisibility(View.GONE);
            holder.animationAccept.setVisibility(View.GONE);
            holder.indicator.setVisibility(View.VISIBLE);
            holder.indicator.setText("Accepted");
            notifyDataSetChanged();
        });

        holder.animationReject.setOnClickListener(view -> {
            RequestReference.child(request.getPushId()).removeValue();
            holder.animationReject.setVisibility(View.GONE);
            holder.animationAccept.setVisibility(View.GONE);
            holder.indicator.setVisibility(View.VISIBLE);
            holder.indicator.setText("Rejected");
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return requestArrayList.size();
    }

    public class RequestsViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView RequestImage;
        private TextView RequestName, TrackRequest, indicator;
        private LottieAnimationView animationAccept, animationReject;

        public RequestsViewHolder(@NonNull View itemView) {
            super(itemView);
            RequestImage = itemView.findViewById(R.id.RequestImage);
            RequestName = itemView.findViewById(R.id.RequestName);
            TrackRequest = itemView.findViewById(R.id.TrackRequest);
            animationAccept = itemView.findViewById(R.id.animationAccept);
            animationReject = itemView.findViewById(R.id.animationReject);
            indicator = itemView.findViewById(R.id.indicator);

        }
    }
}
