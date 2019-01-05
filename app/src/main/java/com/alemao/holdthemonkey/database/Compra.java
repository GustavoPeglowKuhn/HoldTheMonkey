package com.alemao.holdthemonkey.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "compra")
public class Compra {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "custo")
    public float custo;

    @ColumnInfo(name = "categoria")
    public String categoria;

    @ColumnInfo(name = "detalhes")
    public String detalhes;

    //data
    @ColumnInfo(name = "dia")
    public int  dia;
    @ColumnInfo(name = "mes")
    public int  mes;
    @ColumnInfo(name = "ano")
    public int  ano;

    public Compra(String categoria, float custo, String detalhes) {
        this.categoria = categoria;
        this.custo = custo;
        this.detalhes = detalhes;

        Calendar calendar = Calendar.getInstance();
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        ano = calendar.get(Calendar.YEAR);
    }
}