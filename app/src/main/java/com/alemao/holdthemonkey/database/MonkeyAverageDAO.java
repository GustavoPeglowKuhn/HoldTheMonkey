package com.alemao.holdthemonkey.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MonkeyAverageDAO {
    @Query("SELECT categoria, SUM(custo) AS custo FROM compra GROUP BY categoria")
    List<MonkeyAverage> getSum();
}
