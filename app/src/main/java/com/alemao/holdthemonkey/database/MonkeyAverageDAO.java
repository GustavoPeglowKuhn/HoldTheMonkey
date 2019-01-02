package com.alemao.holdthemonkey.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MonkeyAverageDAO {
    @Query("SELECT categoria, custo FROM compra")
    List<MonkeyAverage> getAll();

    @Query("SELECT categoria, SUM(custo) AS custo FROM compra GROUP BY categoria")
    List<MonkeyAverage> getSum();

    @Query("SELECT categoria, custo FROM compra WHERE ano = :ano AND mes = :mes")
    List<MonkeyAverage> getAll(int mes, int ano);

    @Query("SELECT categoria, SUM(custo) AS custo FROM compra WHERE ano = :ano AND mes = :mes GROUP BY categoria")
    List<MonkeyAverage> getSum(int mes, int ano);
}
