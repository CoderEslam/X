package com.doubleclick.x_course.Adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.doubleclick.x_course.Chat.ViewActivity;
import com.doubleclick.x_course.Model.Chat;
import com.doubleclick.x_course.R;
import com.doubleclick.x_course.ViewRecord.VoicePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;

    FirebaseUser fuser;

    public MessageAdapter() {
    }

    public MessageAdapter(Context mContext, List<Chat> mChat, String imageurl) {
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_right, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_left, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Chat chat = mChat.get(position);
        try {
            if (chat.getType().isEmpty() || chat.getType() == null) {
                holder.show_message.setText(chat.getMessage());
                holder.imageSent.setVisibility(View.GONE);
            } else {
                if (chat.getType().equals("text")){
                    holder.imageSent.setVisibility(View.GONE);
                    holder.show_message.setText(chat.getMessage());
                } else if (chat.getType().equals("image")){
                    holder.show_message.setVisibility(View.GONE);
                    holder.imageSent.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(chat.getMessage()).placeholder(R.drawable.account_circle_24).into(holder.imageSent);
                }else if (chat.getType().equals("voice")){
                    holder.show_message.setVisibility(View.GONE);
                    holder.imageSent.setVisibility(View.GONE);
                    holder.voicePlayerView.setVisibility(View.VISIBLE);
                    holder.voicePlayerView.setAudio(chat.getMessage());
                }
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        if (imageurl.equals("default")) {
            holder.profile_image.setImageResource(R.drawable.account_circle_24);
        } else {
            Glide.with(mContext).load(imageurl).placeholder(R.drawable.account_circle_24).into(holder.profile_image);
        }

        if (position == mChat.size() - 1) {
            if (chat.isIsseen()) {
                holder.txt_seen.setText("Seen");
            } else {
                holder.txt_seen.setText("Delivered");
            }
        } else {
            holder.txt_seen.setVisibility(View.GONE);
        }

        holder.imageSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(holder.itemView.getContext(), ViewActivity.class);
                intent.putExtra("image",chat.getMessage());
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView show_message;
        public ImageView profile_image;
        public TextView txt_seen;
        public ImageView imageSent;
        public VoicePlayerView voicePlayerView;

        public ViewHolder(View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
            imageSent = itemView.findViewById(R.id.imageSent);
            voicePlayerView = itemView.findViewById(R.id.voicePlayerView);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipboardManager clipboardManager = (ClipboardManager) itemView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setText(mChat.get(getAdapterPosition()).getMessage());
                    Toast.makeText(itemView.getContext(), "Copied", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
