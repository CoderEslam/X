package com.doubleclick.x_course.Repositorys;

import android.util.Log;

import com.doubleclick.x_course.Model.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatsRepository {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String myID;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    List<Chat> chatList = new ArrayList<>();
    OnMessageAdded interfaceformessages;

    public ChatsRepository(OnMessageAdded interfaceformessages) {
        this.interfaceformessages = interfaceformessages;
    }

    public void getAllMessages(String friend) {
        myID = mAuth.getCurrentUser().getUid();
        firestore.collection("ChatList").orderBy("time").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
                chatList.clear();
                for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                    Chat messageModel = documentSnapshot.toObject(Chat.class);
                    // we only want to display conversation between two users
                    // since every user will have diffferent conversation
                    if (messageModel.getSender().equals(myID) && messageModel.getReceiver().equals(friend) ||
                            messageModel.getReceiver().equals(myID) && messageModel.getSender().equals(friend)) {
                        chatList.add(messageModel);

                    }
                }
                Log.e("ChatsRepository = ",chatList.toString());
                interfaceformessages.MessagesListener(chatList);
            }
        });
    }

    public interface OnMessageAdded {
        void MessagesListener(List<Chat> messageModels);
    }


}
