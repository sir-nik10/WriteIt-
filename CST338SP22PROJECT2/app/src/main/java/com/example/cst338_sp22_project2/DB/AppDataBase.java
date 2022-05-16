package com.example.cst338_sp22_project2.DB;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.cst338_sp22_project2.DAO.Post;
import com.example.cst338_sp22_project2.DAO.User;

@Database(entities = {User.class, Post.class}, version = 2)

public abstract class AppDataBase extends RoomDatabase {
    public static final String DATABASE_NAME = "writeIt.DB";
    public static final String USER_TABLE = "USER_TABLE";
    public static final String POST_TABLE = "POST_TABLE";
    private static volatile AppDataBase instance;
    private static final Object LOCK  = new Object();

    public abstract writeItDAO mWriteItDAO();

    public static AppDataBase getInstance(Context context){
        if(instance ==null){
            synchronized (LOCK){
                if(instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),AppDataBase.class,DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }

}
