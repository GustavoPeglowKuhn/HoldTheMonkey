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

    @Query("SELECT categoria, custo FROM compra WHERE ano = :ano")
    List<MonkeyAverage> getAll(int ano);
    @Query("SELECT categoria, SUM(custo) AS custo FROM compra WHERE ano = :ano GROUP BY categoria")
    List<MonkeyAverage> getSum(int ano);

    @Query("SELECT categoria, custo FROM compra WHERE ano = :ano AND mes = :mes")
    List<MonkeyAverage> getAll(int mes, int ano);
    @Query("SELECT categoria, SUM(custo) AS custo FROM compra WHERE ano = :ano AND mes = :mes GROUP BY categoria")
    List<MonkeyAverage> getSum(int mes, int ano);

    @Query("SELECT SUM(custo) FROM compra")
    float getTotalSum();
    @Query("SELECT SUM(custo) FROM compra WHERE ano = :ano")
    float getTotalSum(int ano);
    @Query("SELECT SUM(custo) FROM compra WHERE ano = :ano AND mes = :mes")
    float getTotalSum(int mes, int ano);
}
