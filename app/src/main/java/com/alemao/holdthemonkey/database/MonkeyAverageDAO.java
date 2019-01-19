package com.alemao.holdthemonkey.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MonkeyAverageDAO {
    @Query("SELECT categoria, custo FROM compra ORDER BY ano DESC, mes DESC, dia DESC")
    List<MonkeyAverage> getAll();
    @Query("SELECT categoria, SUM(custo) AS custo FROM compra GROUP BY categoria ORDER BY ano DESC, mes DESC, dia DESC")
    List<MonkeyAverage> getSum();

    @Query("SELECT categoria, custo FROM compra WHERE ano = :ano ORDER BY mes DESC, dia DESC")
    List<MonkeyAverage> getAll(int ano);
    @Query("SELECT categoria, SUM(custo) AS custo FROM compra WHERE ano = :ano GROUP BY categoria ORDER BY mes DESC, dia DESC")
    List<MonkeyAverage> getSum(int ano);

    @Query("SELECT categoria, custo FROM compra WHERE ano = :ano AND mes = :mes ORDER BY dia DESC")
    List<MonkeyAverage> getAll(int mes, int ano);
    @Query("SELECT categoria, SUM(custo) AS custo FROM compra WHERE ano = :ano AND mes = :mes GROUP BY categoria ORDER BY dia DESC")
    List<MonkeyAverage> getSum(int mes, int ano);

    @Query("SELECT SUM(custo) FROM compra")
    float getTotalSum();
    @Query("SELECT SUM(custo) FROM compra WHERE ano = :ano")
    float getTotalSum(int ano);
    @Query("SELECT SUM(custo) FROM compra WHERE ano = :ano AND mes = :mes")
    float getTotalSum(int mes, int ano);
}
