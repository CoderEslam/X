package com.doubleclick.x_course.RoomDataBase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.doubleclick.x_course.Model.Chat;

import java.util.List;

public class ChatRepositry {

    private ChatDAO chatDAO;
    private LiveData<List<Chat>> getChat;
    private Context context;


    public ChatRepositry(Context app,String myID,String FriendId) {
        context = app;
        ChatRoomDB db = ChatRoomDB.getInstance(app);
        chatDAO = db.chatDAO();
        getChat = chatDAO.getAllChat(myID, FriendId);

    }

    //insert
    public void insert(Chat chat) {
        new InsertAsyncTask(chatDAO).execute(chat);
    }

    //delete
    public void delete(Chat chat) {
        new DeleteAsyncTask(chatDAO).execute(chat);
    }

    //update
    public void update(Chat chat) { //done
        new UpdatetAsyncTask(chatDAO).execute(chat);
    }

    //getAllWords
    public LiveData<List<Chat>> getAllChats() {
        return getChat;
    }

    //deleteAllWords
    public void deleteAllWords() {
        new DeleteAllAsyncTask(chatDAO).execute();
    }

    private static class InsertAsyncTask extends AsyncTask<Chat, Void, Void> {

        private ChatDAO chatDAO;

        public InsertAsyncTask(ChatDAO chatDAO) {
            this.chatDAO = chatDAO;
        }

        @Override
        protected Void doInBackground(Chat... chats) {
            chatDAO.insert(chats[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Chat, Void, Void> {

        private ChatDAO chatDAO;

        public DeleteAsyncTask(ChatDAO chatDAO) {
            this.chatDAO = chatDAO;
        }

        @Override
        protected Void doInBackground(Chat... chats) {
            chatDAO.delete(chats[0]);
            return null;
        }
    }

    private static class UpdatetAsyncTask extends AsyncTask<Chat, Void, Void> {

        private ChatDAO chatDAO;

        public UpdatetAsyncTask(ChatDAO chatDAO) {
            this.chatDAO = chatDAO;
        }

        @Override
        protected Void doInBackground(Chat... chats) {
            chatDAO.update(chats[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private ChatDAO chatDAO;

        public DeleteAllAsyncTask(ChatDAO chatDAO) {
            this.chatDAO = chatDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            chatDAO.deleteAllData();

            return null;
        }
    }

}
