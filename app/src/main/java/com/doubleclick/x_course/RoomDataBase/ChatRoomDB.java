package com.doubleclick.x_course.RoomDataBase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.doubleclick.x_course.Model.Chat;


@Database(entities = Chat.class,version = 1)
public abstract class ChatRoomDB extends RoomDatabase {

    private static ChatRoomDB instance;

    public abstract ChatDAO chatDAO();

    //Singlton
    public static synchronized ChatRoomDB getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ChatRoomDB.class,"ChatDatabase")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){

        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            super.onCreate(db);
            new pupulatDataAsyncTask(instance).execute();
        }

        @Override
        public void onOpen( SupportSQLiteDatabase db) {
            super.onOpen(db);
        }

    };


    private static class pupulatDataAsyncTask extends AsyncTask<Void,Void,Void> {
        private ChatDAO chatDAO;
        pupulatDataAsyncTask(ChatRoomDB db){
            chatDAO = db.chatDAO();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }



}
