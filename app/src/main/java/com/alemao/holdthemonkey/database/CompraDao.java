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
    @Query("SELECT * FROM compra WHERE ano = :ano")
    List<Compra> getAll(int ano);
    @Query("SELECT * FROM compra WHERE ano = :ano AND mes = :mes")
    List<Compra> getAll(int mes, int ano);

    @Query("UPDATE compra SET custo = :custo, categoria = :categoria, detalhes = :detalhes WHERE id = :id")
    void updateItem(int id, float custo, String categoria, String detalhes);

    @Query("DELETE FROM compra WHERE id = :id")
    void deleteItem(int id);

    @Insert
    void insertAll(Compra... users);

    @Delete
    void delete(Compra user);
}