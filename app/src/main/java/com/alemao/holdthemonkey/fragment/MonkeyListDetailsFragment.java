package com.alemao.holdthemonkey.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alemao.holdthemonkey.R;
import com.alemao.holdthemonkey.activity.MainActivity;
import com.alemao.holdthemonkey.adapter.MonkeyDetailListItemAdapter;
import com.alemao.holdthemonkey.database.AppDatabase;
import com.alemao.holdthemonkey.database.Compra;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonkeyListDetailsFragment extends Fragment {

    public  ListView listView;
    AdapterView.OnItemClickListener icl;
    private ArrayList<Compra> monkeys;
    private MonkeyDetailListItemAdapter adapter;

    int _dia, _mes, _ano;

    public MonkeyListDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monkey_list_details, container, false);

        listView = view.findViewById(R.id.fmld_lv_categories);
        if(icl!=null) listView.setOnItemClickListener(icl);
        monkeys = new ArrayList<>();

        adapter = new MonkeyDetailListItemAdapter(getActivity(), monkeys);
        listView.setAdapter(adapter);

        return view;
    }

    public void configItemClick(final MainActivity mainActivity){

        icl = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getLayoutInflater();

                final View v = inflater.inflate(R.layout.dialog_add_compra, null);
                alertDialog.setView(v);

                final Compra compra = monkeys.get(position);
                final EditText txtCusto = v.findViewById(R.id.dac_edit_custo);
                final EditText txtCategoria = v.findViewById(R.id.dac_edit_categoria);
                final EditText txtDetahles = v.findViewById(R.id.dac_edit_detahles);

                final RadioButton rbToday = v.findViewById(R.id.dac_rb_today);
                final RadioButton rbCustomDate = v.findViewById(R.id.dac_rb_custom_date);
                final TextView txtCustomDate = v.findViewById(R.id.dac_txt_custom_date);

                txtCusto.setText(""+compra.custo);
                txtCategoria.setText(compra.categoria);
                txtDetahles.setText(compra.detalhes);

                rbToday.setChecked(false);
                rbCustomDate.setChecked(true);
                rbCustomDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(rbCustomDate.isChecked()){
                            txtCustomDate.setEnabled(true);
                            txtCustomDate.setTextColor(getResources().getColor(R.color.link));
                        }else{
                            txtCustomDate.setEnabled(false);
                            txtCustomDate.setTextColor(getResources().getColor(R.color.linkDisable));
                        }
                    }
                });
                _dia = compra.dia;
                _mes = compra.mes;
                _ano = compra.ano;
                txtCustomDate.setEnabled(true);
                txtCustomDate.setTextColor(getResources().getColor(R.color.link));
                txtCustomDate.setText(getString(R.string.date_formater, _dia, _mes+1, _ano));
                txtCustomDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        com.alemao.holdthemonkey.dialog.DatePickerDialog datePickerDialog = new com.alemao.holdthemonkey.dialog.DatePickerDialog(mainActivity, getLayoutInflater()) {
                            @Override
                            public void OnDataSet() {
                                _dia = getDia();
                                _mes = getMes();
                                _ano = getAno();

                                txtCustomDate.setText(getString(R.string.date_formater, _dia, _mes+1, _ano));
                            }
                        };
                    }
                });

                alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });

                alertDialog.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mainActivity.deleteItem(compra.id);
                    }
                });

                final AlertDialog.Builder edit = alertDialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (txtCusto.getText().length() == 0) {
                            Toast.makeText(getActivity(), "Digite o novo custo!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (txtCategoria.getText().length() == 0) {
                            Toast.makeText(getActivity(), "Digite a nova categoria!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        float custo = Float.parseFloat(txtCusto.getText().toString());
                        String categoria = txtCategoria.getText().toString();
                        String detalhes = txtDetahles.getText().toString();

                        Compra c = new Compra();
                        c.id = compra.id;
                        c.categoria = categoria;
                        c.custo = custo;
                        c.detalhes = detalhes;
                        if(rbCustomDate.isChecked()){
                            c.dia = _dia;
                            c.mes = _mes;
                            c.ano = _ano;
                        }else{
                            Calendar calendar = Calendar.getInstance();
                            c.dia = calendar.get(Calendar.DAY_OF_MONTH);
                            c.mes = calendar.get(Calendar.MONTH);
                            c.ano = calendar.get(Calendar.YEAR);
                        }

                        mainActivity.updateItem(c);
                    }
                });

                alertDialog.show();
            }
        };
        if(listView!=null)listView.setOnItemClickListener(icl);
    }

    public void updateList(List<Compra> list){
        monkeys.clear();
        if(list!=null) {
            monkeys.addAll(list);       //for (Compra item : list) monkeys.add(item);
        }
        adapter.notifyDataSetChanged();
    }
}
