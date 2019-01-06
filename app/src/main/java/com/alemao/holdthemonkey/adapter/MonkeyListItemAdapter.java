package com.alemao.holdthemonkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alemao.holdthemonkey.R;
import com.alemao.holdthemonkey.model.MonkeyListItem;

import java.util.ArrayList;

public class MonkeyListItemAdapter extends ArrayAdapter<MonkeyListItem> {

    private ArrayList<MonkeyListItem> list;
    private Context context;

    public MonkeyListItemAdapter(Context c, ArrayList<MonkeyListItem> objects) {
        super(c, 0, objects);
        this.list = objects;
        this.context = c;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        View view = null;

        if(list != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.monkey_list_item, parent, false);

            TextView txtCusto = view.findViewById(R.id.mli_txt_custo);
            TextView txtCategoria = view.findViewById(R.id.mli_txt_categoria);

            MonkeyListItem item = list.get(position);

            txtCategoria.setText(item.getCategorie());

            String custo;

            if(item.getCusto()>0){
                custo = "R$ "+item.getCusto();
                txtCusto.setTextColor(context.getResources().getColor(R.color.gasto));
            }else{
                custo = "R$ "+(-item.getCusto());
                txtCusto.setTextColor(context.getResources().getColor(R.color.ganho));
            }

            if(custo.lastIndexOf(".") == (custo.length()-2))
                custo+="0";
            txtCusto.setText(custo);
        }

        return view;
    }
}
