package com.sabirov.client;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Message.class}, version = 1, exportSchema = false)
public abstract class DataBase extends RoomDatabase {
    private static DataBase dataBase;

    public synchronized static DataBase getInstance(Context context){
        if (dataBase==null){
            String DATABASE_NAME = "database";
            dataBase= Room.databaseBuilder(context.getApplicationContext(), DataBase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build();
        }
        return dataBase;
    }

    public abstract MainDao mainDao();
}
