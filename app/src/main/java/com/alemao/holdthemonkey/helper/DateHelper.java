package com.alemao.holdthemonkey.helper;

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
}
