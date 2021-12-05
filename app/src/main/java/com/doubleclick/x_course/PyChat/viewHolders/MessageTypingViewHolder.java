package com.doubleclick.x_course.PyChat.viewHolders;


import android.view.View;

import com.doubleclick.x_course.PyChat.models.AttachmentTypes;

public class MessageTypingViewHolder extends BaseMessageViewHolder {
    public MessageTypingViewHolder(View itemView) {
        super(itemView, AttachmentTypes.NONE_TYPING,null);
    }
}
