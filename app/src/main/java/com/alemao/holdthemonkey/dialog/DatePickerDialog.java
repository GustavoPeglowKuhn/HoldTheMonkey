package com.alemao.holdthemonkey.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.alemao.holdthemonkey.R;

abstract public class DatePickerDialog {

    Context c;
    LayoutInflater inf;

    public int dia, mes, ano;

    public DatePickerDialog(Context context, LayoutInflater inflater) {
        c = context;
        inf = inflater;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);

        final View v = inf.inflate(R.layout.dialog_date_picker, null);
        final DatePicker datePicker = v.findViewById(R.id.datePicker);

        //datePicker.updateDate(2019, 01, 01);  //receber a data inicial no construtor

        //int curDay   = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        //int curMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
        //int curYear  = Calendar.getInstance().get(Calendar.YEAR);

        alertDialog.setView(v);

        alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dia = datePicker.getDayOfMonth();
                mes = datePicker.getMonth();
                ano = datePicker.getYear();
                OnDataSet();
            }
        });

        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }

    public int getDia(){ return dia; }
    public int getMes() { return mes; }
    public int getAno() { return ano; }

    abstract public void OnDataSet();
}
