package com.alemao.holdthemonkey.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alemao.holdthemonkey.R;
import com.alemao.holdthemonkey.database.Compra;

import java.util.ArrayList;

public class MonkeyDetailListItemAdapter extends ArrayAdapter<Compra> {

    private ArrayList<Compra> list;
    private Context context;

    public MonkeyDetailListItemAdapter(Context c, ArrayList<Compra> objects) {
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

            view = inflater.inflate(R.layout.monkey_list_item_details, parent, false);

            TextView txtCusto       = view.findViewById(R.id.mlid_txt_custo);
            TextView txtCategoria   = view.findViewById(R.id.mlid_txt_categoria);
            TextView txtDetahles    = view.findViewById(R.id.mlid_txt_detalhes);
            TextView txtData        = view.findViewById(R.id.mlid_txt_data);


            Compra item = list.get(position);

            String mes;
            if(item.mes>8)  mes = "/"+(item.mes+1)+"/";
            else            mes = "/0"+(item.mes+1)+"/";

            txtCategoria.setText(item.categoria);
            txtDetahles.setText(item.detalhes);
            txtData.setText(""+item.dia+mes+item.ano);

            String custo;

            if(item.custo<0){
                custo = "R$ "+(-item.custo);
                txtCusto.setTextColor(context.getResources().getColor(R.color.ganho));
            }else if(item.custo>0){
                custo = "R$ "+item.custo;
                txtCusto.setTextColor(context.getResources().getColor(R.color.gasto));
            }else{
                custo = "R$ "+item.custo;
                txtCusto.setTextColor(context.getResources().getColor(R.color.black));
            }

            int pontoPos = custo.lastIndexOf(".");
            if(pontoPos == (custo.length()-2))
                custo+="0";
            else if(pontoPos < (custo.length()-3))
                custo = custo.substring(0, pontoPos+3);
            txtCusto.setText(custo);
        }

        return view;
    }
}
