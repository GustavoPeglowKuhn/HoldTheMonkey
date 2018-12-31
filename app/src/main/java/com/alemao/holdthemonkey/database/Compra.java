package com.alemao.holdthemonkey.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "compra")
public class Compra {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "categoria")
    public String categoria;

    @ColumnInfo(name = "custo")
    public float  custo;

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
        this.dia = 1;
        this.mes = 1;
        this.ano = 2020;
    }
}