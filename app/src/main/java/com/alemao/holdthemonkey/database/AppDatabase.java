package com.alemao.holdthemonkey.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Compra.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CompraDao compraDao();
    public abstract MonkeyAverageDAO monkeyAverageDAO();
}