package com.alemao.holdthemonkey.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Compra.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CompraDao compraDao();
    public abstract MonkeyAverageDAO monkeyAverageDAO();
    private static AppDatabase db;
    private static Context context;

    public static AppDatabase getDb(){
        if(db==null){
            db = Room.databaseBuilder(context,
                    AppDatabase.class, "monkey_db").build();
        }
        return db;
    }

    public static void setContext(Context context) {
        AppDatabase.context = context;
    }
}