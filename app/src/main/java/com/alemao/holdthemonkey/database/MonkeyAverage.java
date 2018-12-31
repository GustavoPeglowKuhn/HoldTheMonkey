package com.alemao.holdthemonkey.database;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

@Entity
public class MonkeyAverage {

    @ColumnInfo(name = "categoria")
    public String categoria;

    @ColumnInfo(name = "custo")
    public float custo;

    public String getCategoria() {
        return categoria;
    }
}