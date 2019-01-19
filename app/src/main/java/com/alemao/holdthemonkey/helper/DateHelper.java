package com.alemao.holdthemonkey.helper;

import android.content.Context;

import com.alemao.holdthemonkey.R;

import java.util.Calendar;

public class DateHelper {
    static public boolean isInFuture(int dia, int mes, int ano){
        Calendar calendar = Calendar.getInstance();
        int _dia = calendar.get(Calendar.DAY_OF_MONTH);
        int _mes = calendar.get(Calendar.MONTH);
        int _ano = calendar.get(Calendar.YEAR);

        if(ano< _ano) return false;
        if(ano> _ano) return true;

        if(mes< _mes) return false;
        if(mes> _mes) return true;

        if(dia<=_dia) return false;
        return true;
    }

    static public boolean isToday(int dia, int mes, int ano){
        Calendar calendar = Calendar.getInstance();
        int _dia = calendar.get(Calendar.DAY_OF_MONTH);
        int _mes = calendar.get(Calendar.MONTH);
        int _ano = calendar.get(Calendar.YEAR);

        return _dia==dia && _mes==mes && _ano==ano;
    }
    
    static public String getMonth(Context context, int month){
        switch (month){
            case  0: return context.getString(R.string.lbl_jan);
            case  1: return context.getString(R.string.lbl_feb);
            case  2: return context.getString(R.string.lbl_mar);
            case  3: return context.getString(R.string.lbl_may);
            case  4: return context.getString(R.string.lbl_apr);
            case  5: return context.getString(R.string.lbl_jun);
            case  6: return context.getString(R.string.lbl_jul);
            case  7: return context.getString(R.string.lbl_aug);
            case  8: return context.getString(R.string.lbl_sep);
            case  9: return context.getString(R.string.lbl_oct);
            case 10: return context.getString(R.string.lbl_nov);
            case 11: return context.getString(R.string.lbl_dec);
        }
        return null;
    }
}
