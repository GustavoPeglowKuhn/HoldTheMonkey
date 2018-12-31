package com.alemao.holdthemonkey.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CompraDao {    //Data Access Object = DAO
    @Query("SELECT categoria FROM compra GROUP BY categoria")
    List<String> getAllCategories();

    @Query("SELECT * FROM compra")
    List<Compra> getAll();
    @Query("SELECT * FROM compra WHERE categoria LIKE :category")
    List<Compra> getAll(String category);

    @Query("SELECT SUM(custo) FROM compra WHERE categoria LIKE :category")
    float getSum(String category);
    @Query("SELECT AVG(custo) FROM compra WHERE categoria LIKE :category")
    float getAvg(String category);

    //TODO: limitar a query por data
    @Query("SELECT SUM(custo) FROM compra WHERE categoria LIKE :category AND ano = :ano AND mes = :mes")
    float getSum(String category, int ano, int mes);
    @Query("SELECT AVG(custo) FROM compra WHERE categoria LIKE :category AND ano = :ano AND mes = :mes")
    float getAvg(String category, int ano, int mes);

    @Insert
    void insertAll(Compra... users);

    @Delete
    void delete(Compra user);
}