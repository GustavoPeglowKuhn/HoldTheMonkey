package com.alemao.holdthemonkey.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "compra")
public class Compra {
    @PrimaryKey(autoGenerate = true)
    public int id;

    /*if custo>0: e uma despesa
    * if custo<0: e um lucro (tipo salario e outras entradas)*/
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

    public Compra(){}
}