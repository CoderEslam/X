package com.doubleclick.x_course.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.doubleclick.x_course.AboutCourseActivity;
import com.doubleclick.x_course.CourseActivity;
import com.doubleclick.x_course.Model.Diploma;
import com.doubleclick.x_course.Model.Emails;
import com.doubleclick.x_course.Model.Request;
import com.doubleclick.x_course.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DiplomasAdapter extends RecyclerView.Adapter<DiplomasAdapter.DiplomasViewHolder> {

    private ArrayList<Diploma> diplomas = new ArrayList<>();
    private ArrayList<Request> requests = new ArrayList<>();
    private DatabaseReference referenceRate, referenceEmails, RequestRef;
    private String email_user;
    private String UserId;
    private String UserName;
    private String imageURL;
    private String keyJoin = "join";
    private String[] Web;
    private String[] Mobile;
    private String[] GraphicDesign;
    private String track = "Choose";
    private boolean check;
    private Intent AboutIntent;
    private FirebaseAuth mAuth;


    public DiplomasAdapter(ArrayList<Diploma> diplomas, String email, String UserId, String userName, String imageURL, Context context) {
        this.diplomas = diplomas;
        this.email_user = email;
        this.UserId = UserId;
        this.UserName = userName;
        this.imageURL = imageURL;
        mAuth = FirebaseAuth.getInstance();
        referenceRate = FirebaseDatabase.getInstance().getReference().child("Rates");
        referenceEmails = FirebaseDatabase.getInstance().getReference().child("Emails");
        RequestRef = FirebaseDatabase.getInstance().getReference().child("Requests");
        AboutIntent = new Intent(context, AboutCourseActivity.class);

    }


    @NonNull
    @Override
    public DiplomasAdapter.DiplomasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diploma, parent, false);
        return new DiplomasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiplomasAdapter.DiplomasViewHolder holder, int position) {

        Web = holder.itemView.getResources().getStringArray(R.array.Web);
        Mobile = holder.itemView.getResources().getStringArray(R.array.Mobile);
        GraphicDesign = holder.itemView.getResources().getStringArray(R.array.graphicDesign);
        if (diplomas.get(holder.getAdapterPosition()).getNameOfDiploma().equals("Web")) {
            ArrayAdapter aa = new ArrayAdapter(holder.itemView.getContext(), R.layout.item_spinner, R.id.tv_selected, Web);
            holder.spinnerTrack.setAdapter(aa);
        } else if (diplomas.get(holder.getAdapterPosition()).getNameOfDiploma().equals("Mobile")) {
            Toast.makeText(holder.itemView.getContext(), "" + diplomas.get(holder.getAdapterPosition()).getNameOfDiploma(), Toast.LENGTH_SHORT).show();
            ArrayAdapter aa = new ArrayAdapter(holder.itemView.getContext(), R.layout.item_spinner, R.id.tv_selected, Mobile);
            holder.spinnerTrack.setAdapter(aa);
        } else if (diplomas.get(holder.getAdapterPosition()).getNameOfDiploma().equals("graphicDesign")) {
            ArrayAdapter aa = new ArrayAdapter(holder.itemView.getContext(), R.layout.item_spinner, R.id.tv_selected, GraphicDesign);
            holder.spinnerTrack.setAdapter(aa);
        }
        try {
            if (diplomas.get(holder.getAdapterPosition()).getImageOfDiploma() == null || diplomas.get(holder.getAdapterPosition()).getImageOfDiploma().equals("")) {
                int[] computer = new int[]{R.raw.computer_science_x, R.raw.programming_computer1, R.raw.man_working_on_computer};
                Random random = new Random();
                int number = random.nextInt(3);
                holder.animation.setAnimation(computer[number]);
            } else {
                Glide.with(holder.itemView.getContext()).load(diplomas.get(holder.getAdapterPosition()).getImageOfDiploma()).placeholder(R.drawable.loading_icon).into(holder.imageOfDiploma);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        holder.nameOfDevelpoer.setText(diplomas.get(holder.getAdapterPosition()).getNameOfDevelpoer());
        holder.tv_nameOfDiploma.setText(diplomas.get(holder.getAdapterPosition()).getNameOfDiploma() + " " + diplomas.get(holder.getAdapterPosition()).getNumberOfDiploma());


        holder.spinnerTrack.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if (diplomas.get(holder.getAdapterPosition()).getNameOfDiploma().equals("Web")) {
                    track = Web[pos];
                    CheckRequested(holder, holder.getAdapterPosition(), track);
                } else if (diplomas.get(holder.getAdapterPosition()).getNameOfDiploma().equals("Mobile")) {
                    track = Mobile[pos];
                    CheckRequested(holder, holder.getAdapterPosition(), track);
                } else if (diplomas.get(holder.getAdapterPosition()).getNameOfDiploma().equals("graphicDesign")) {
                    track = GraphicDesign[pos];
                    CheckRequested(holder, holder.getAdapterPosition(), track);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (diplomas.get(holder.getAdapterPosition()).getNameOfDiploma().equals("Web")) {
                    track = Web[0];
                } else if (diplomas.get(holder.getAdapterPosition()).getNameOfDiploma().equals("Mobile")) {
                    track = Mobile[0];
                } else if (diplomas.get(holder.getAdapterPosition()).getNameOfDiploma().equals("graphicDesign")) {
                    track = GraphicDesign[0];
                }
            } //Done
        }); //Done

        holder.join.setOnClickListener(view -> {
            if (mAuth != null) {
                if (track.equals("Choose") && !keyJoin.equals("Watch")) {
                    Toast.makeText(holder.itemView.getContext(), "you have to choose track first", Toast.LENGTH_LONG).show();
                } else if (keyJoin.equals("Delete") || keyJoin.equals("join") || keyJoin.equals("Watch")) {
                    askJoin(holder);
                }
            }
        }); //Done

        holder.itemView.setOnClickListener(view -> {
            if (mAuth != null) {
                try {
                    AboutIntent.putExtra("nameOfDiploma", diplomas.get(holder.getAdapterPosition()).getNameOfDiploma());
                    AboutIntent.putExtra("nameOfDevelpoer", diplomas.get(holder.getAdapterPosition()).getNameOfDevelpoer());
                    AboutIntent.putExtra("lastPrice", diplomas.get(holder.getAdapterPosition()).getLastPrice());
                    AboutIntent.putExtra("newPrice", diplomas.get(holder.getAdapterPosition()).getNewPrice());
                    AboutIntent.putExtra("imageOfDiploma", diplomas.get(holder.getAdapterPosition()).getImageOfDiploma());
                    AboutIntent.putExtra("animationDiscount", diplomas.get(holder.getAdapterPosition()).getUrlAnimation());
                    AboutIntent.putExtra("aboutCourses", diplomas.get(holder.getAdapterPosition()).getAboutCourse());
                    AboutIntent.putExtra("numberOfDiploma", diplomas.get(holder.getAdapterPosition()).getNumberOfDiploma());
                    AboutIntent.putExtra("promo_youtube", diplomas.get(holder.getAdapterPosition()).getPromo_youtube());
                    AboutIntent.putExtra("UserId", UserId);
                    AboutIntent.putExtra("timestamp", diplomas.get(holder.getAdapterPosition()).getTimestamp());
                    AboutIntent.putExtra("email", email_user);
                    Log.e("DiplomasAdapter = ", diplomas.get(holder.getAdapterPosition()).getNameOfDiploma());
                    holder.itemView.getContext().startActivity(AboutIntent);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

        CheckSubscription(holder, holder.getAdapterPosition());
        check = CheckRequested(holder, holder.getAdapterPosition(), "");

    }


    private void askJoin(DiplomasAdapter.DiplomasViewHolder holder) {
        if (mAuth != null) {
            if (keyJoin.equals("join")) {
                holder.join.setText("Delete");
                holder.join.setEnabled(true);
                String PushId = UserId + ":" + diplomas.get(holder.getAdapterPosition()).getNameOfDiploma() + ":" + diplomas.get(holder.getAdapterPosition()).getNumberOfDiploma();
                Map<String, Object> mapRequest = new HashMap<>();
                mapRequest.put("nameOfDiploma", diplomas.get(holder.getAdapterPosition()).getNameOfDiploma());
                mapRequest.put("numberOfDiploma", diplomas.get(holder.getAdapterPosition()).getNumberOfDiploma());
                mapRequest.put("userId", UserId);
                mapRequest.put("track", track);
                mapRequest.put("username", UserName);
                mapRequest.put("imageURL", imageURL);
                mapRequest.put("timestamp", diplomas.get(holder.getAdapterPosition()).getTimestamp());
                mapRequest.put("NameOfDevelpoer", diplomas.get(holder.getAdapterPosition()).getNameOfDevelpoer());
                mapRequest.put("PushId", PushId);//it's Id in data
                keyJoin = "Delete";
                RequestRef.child(PushId).updateChildren(mapRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(holder.itemView.getContext(), "sent Request", Toast.LENGTH_LONG).show();
                        keyJoin = "Delete";
                        holder.continer.setVisibility(View.INVISIBLE);
                    }
                });

            } else if (keyJoin.equals("Delete")) {
                holder.join.setText("join");
                holder.join.setEnabled(true);
                RemoveRequest(holder);
                keyJoin = "join";
            } else if (keyJoin.equals("Watch")) {
                Intent intent = new Intent(holder.itemView.getContext(), CourseActivity.class);
                holder.itemView.getContext().startActivity(intent);
            }
        }
    }

    @Override
    public int getItemCount() {
        return diplomas.size();
    }

    public class DiplomasViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageOfDiploma;
        private TextView tv_nameOfDiploma, nameOfDevelpoer, join, watch;
        private Spinner spinnerTrack;
        private ConstraintLayout continer;
        private LottieAnimationView animation;

        public DiplomasViewHolder(@NonNull View itemView) {
            super(itemView);
            imageOfDiploma = itemView.findViewById(R.id.imageOfDiploma);
            tv_nameOfDiploma = itemView.findViewById(R.id.nameOfDiploma);
            join = itemView.findViewById(R.id.join);
            animation = itemView.findViewById(R.id.animation);
            nameOfDevelpoer = itemView.findViewById(R.id.nameOfDevelpoer);
            spinnerTrack = itemView.findViewById(R.id.spinnerTrack);
            continer = itemView.findViewById(R.id.continer);
            watch = itemView.findViewById(R.id.watch);

        }
    }

    private void RemoveRequest(DiplomasAdapter.DiplomasViewHolder holder) {
        if (mAuth != null) {
            RequestRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot Snapshot) {
                    for (DataSnapshot dataSnapshot : Snapshot.getChildren()) {
                        Request request = dataSnapshot.getValue(Request.class);
                        if (request.getUserId().equals(UserId)) {
                            RequestRef.child(UserId + ":" + diplomas.get(holder.getAdapterPosition()).getNameOfDiploma() + ":" + diplomas.get(holder.getAdapterPosition()).getNumberOfDiploma()).removeValue();
                        }
                    }
                }
            });
        }
    } //done

    private boolean CheckRequested(DiplomasAdapter.DiplomasViewHolder holder, int postion, String trackPosition) {
        if (mAuth != null) {
            String DiplomaId = diplomas.get(holder.getAdapterPosition()).getNameOfDevelpoer() + ":" + diplomas.get(holder.getAdapterPosition()).getNameOfDiploma() + ":" + diplomas.get(holder.getAdapterPosition()).getNumberOfDiploma() + ":" + diplomas.get(holder.getAdapterPosition()).getTimestamp();
            RequestRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot Snapshot) {
                    for (DataSnapshot dataSnapshot : Snapshot.getChildren()) {
                        Request request = dataSnapshot.getValue(Request.class);
                        requests.add(request);
                        if (request.getDiplomaId().equals(DiplomaId) && request.getNameOfDiploma().equals(diplomas.get(postion).getNameOfDiploma())) {
                            if (request.getTrack().equals(trackPosition) || request.getTrack().equals("")) {
                                keyJoin = "Delete";
                                Log.e("keyJoin = ", keyJoin);
                                holder.continer.setVisibility(View.INVISIBLE);
                                check = true;
                                track = trackPosition;
                                holder.join.setText("Delete");
                                holder.join.setEnabled(true);
                            } else {
                                keyJoin = "join";
                                check = false;
                                holder.join.setText("join");
                                holder.join.setEnabled(true);
                                holder.continer.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            });
        }
        return check;
    }

    private void CheckSubscription(DiplomasAdapter.DiplomasViewHolder holder, int position) {
        if (mAuth != null) {
            String DiplomaId = diplomas.get(holder.getAdapterPosition()).getNameOfDevelpoer() + ":" + diplomas.get(holder.getAdapterPosition()).getNameOfDiploma() + ":" + diplomas.get(holder.getAdapterPosition()).getNumberOfDiploma() + ":" + diplomas.get(holder.getAdapterPosition()).getTimestamp();
            referenceEmails.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Emails mEmail = dataSnapshot.getValue(Emails.class);
                        assert mEmail != null; //make sure mEmail not equal null ,(assert == make sure)
                        if (mEmail.getDiplomaId().equals(DiplomaId)) {
                            holder.join.setText("Watch");
                            keyJoin = "Watch";
                            holder.watch.setVisibility(View.VISIBLE);
                            holder.watch.setText("Watch");
                            holder.join.setVisibility(View.GONE);
                            check = true;
                            holder.join.setEnabled(false);
                            track = mEmail.getTrack();
                            holder.continer.setVisibility(View.GONE);
                            AboutIntent.putExtra("check", check);
                            AboutIntent.putExtra("keyJoin", keyJoin);
                        } else {
                            holder.join.setText("join");
                            keyJoin = "join";
                            check = false;

                        }

                    }
                }
            });
        }
    }

}
