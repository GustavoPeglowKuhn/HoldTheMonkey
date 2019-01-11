package com.alemao.holdthemonkey.activity;

import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alemao.holdthemonkey.R;
import com.alemao.holdthemonkey.database.AppDatabase;
import com.alemao.holdthemonkey.database.Compra;
import com.alemao.holdthemonkey.database.MonkeyAverage;
import com.alemao.holdthemonkey.fragment.MonkeyListDetailsFragment;
import com.alemao.holdthemonkey.fragment.MonkeyListFragment;
import com.alemao.holdthemonkey.helper.SlidingTabLayout;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    //tab titles names
    String[] tabsTitles = {"TOODS", "POR CATEGORIA"};
    MonkeyListFragment monkeysFragment;
    MonkeyListDetailsFragment monkeyDetailsFragment;

    static int dia;
    static int mes;     //de 0 a 11         (-1 nao depende do mes)
    static int ano;     //                  (-1 nao depende do ano)

    int _dia, _mes, _ano;   //retorno do metodo getDate()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.am_toolbar);
        toolbar.setTitle("Hold The Monkey");
        setSupportActionBar(toolbar);

        slidingTabLayout = findViewById(R.id.main_stl);
        viewPager = findViewById(R.id.main_vp);

        monkeysFragment = new MonkeyListFragment();
        monkeyDetailsFragment = new MonkeyListDetailsFragment();

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        FragmentStatePagerAdapter tabAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if(position == 0) return monkeyDetailsFragment;
                if(position == 1) return monkeysFragment;
                return null;
            }

            @Override
            public int getCount() { return tabsTitles.length; }

            @Override
            public CharSequence getPageTitle(int position) { return tabsTitles[position]; }
        };
        viewPager.setAdapter(tabAdapter);
        slidingTabLayout.setViewPager(viewPager);

        dia = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        mes = Calendar.getInstance().get(Calendar.MONTH);
        ano = Calendar.getInstance().get(Calendar.YEAR);

        new UpdateListViewsTask().execute();

        try {
            monkeyDetailsFragment.configItemClick(MainActivity.this);
        }catch(Exception e){
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.mm_item_calendar:
                selectMonth();
                return true;
            case R.id.mm_item_version:
                showVersion();
                return true;
            case R.id.mm_item_help:
                showHelp();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Menu Items
        //filtro de pesquisa
    private void selectMonth(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        final View v = inflater.inflate(R.layout.dialog_month_selector, null);
        final NumberPicker npMonth = v.findViewById(R.id.dms_np_month);
        final NumberPicker npYear  = v.findViewById(R.id.dms_np_year);
        final RadioGroup rg = v.findViewById(R.id.dms_rg);
        final RadioButton rb0 = v.findViewById(R.id.dms_rb0);
        final RadioButton rb1 = v.findViewById(R.id.dms_rb1);
        final RadioButton rb2 = v.findViewById(R.id.dms_rb2);

        int curMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
        int curYear  = Calendar.getInstance().get(Calendar.YEAR);

        npMonth.setMinValue(1);
        npMonth.setMaxValue(12);
        npMonth.setValue(curMonth);

        npYear.setMinValue(2018);
        npYear.setMaxValue(curYear+10);
        npYear.setValue(curYear);

        npMonth.setEnabled(false);
        npYear.setEnabled(false);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(rb0.isChecked()){
                    npMonth.setEnabled(false);
                    npYear.setEnabled(false);
                }else if(rb1.isChecked()){
                    npMonth.setEnabled(false);
                    npYear.setEnabled(true);
                }else{
                    npMonth.setEnabled(true);
                    npYear.setEnabled(true);
                }
            }
        });

        alertDialog.setView(v);

        alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(rb0.isChecked()){
                    mes = -1;
                    ano = -1;
                }else if(rb1.isChecked()){
                    mes = -1;
                    ano = npYear.getValue();
                }else if(rb2.isChecked()){
                    mes = npMonth.getValue()-1;
                    ano = npYear.getValue();
                }else{
                    Toast.makeText(MainActivity.this, "Selecione o periodo!", Toast.LENGTH_SHORT).show();
                    return;
                }
                new UpdateListViewsTask().execute();    //atualiza as listViews
            }
        });

        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }

    private void showVersion(){
        //TODO: chamar um dialog com mais detalhes da versao e mais frescuras
        Toast.makeText(MainActivity.this, R.string.version_name, Toast.LENGTH_SHORT).show();
    }

    private void showHelp(){
        //TODO: chamar um dialog mostrando o texto de help
        Toast.makeText(MainActivity.this, "colocarei na proxima versao", Toast.LENGTH_SHORT).show();
    }
    //!Menu Items!

    //Database
    public void addMonkey(View view){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        final View v = inflater.inflate(R.layout.dialog_add_compra, null);

        final RadioButton rbCustomDate = v.findViewById(R.id.dac_rb_custom_date);
        final TextView txtCustomDate = v.findViewById(R.id.dac_txt_custom_date);

        Calendar calendar = Calendar.getInstance();
        _dia = calendar.get(Calendar.DAY_OF_MONTH);
        _mes = calendar.get(Calendar.MONTH);
        _ano = calendar.get(Calendar.YEAR);
        txtCustomDate.setText(getString(R.string.date_formater, _dia, _mes+1, _ano));

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
        txtCustomDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.alemao.holdthemonkey.dialog.DatePickerDialog datePickerDialog = new com.alemao.holdthemonkey.dialog.DatePickerDialog(MainActivity.this, getLayoutInflater()) {
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

        alertDialog.setView(v);

        alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText txtCusto = v.findViewById(R.id.dac_edit_custo);
                EditText txtCategoria = v.findViewById(R.id.dac_edit_categoria);
                EditText txtDetahles = v.findViewById(R.id.dac_edit_detahles);

                if(txtCusto.getText().length() == 0){
                    Toast.makeText(MainActivity.this, "Digite o custo!", Toast.LENGTH_SHORT).show();;
                    return;
                }
                if(txtCategoria.getText().length() == 0){
                    Toast.makeText(MainActivity.this, "Digite a categoria!", Toast.LENGTH_SHORT).show();;
                    return;
                }

                float custo = Float.parseFloat(txtCusto.getText().toString());
                String categoria = txtCategoria.getText().toString();
                String detalhes = txtDetahles.getText().toString();

                Compra c = new Compra();
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

                new InsertTask().execute(c);
            }
        });

        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });

        alertDialog.show();
    }

    public void updateItem(Compra compra){
        new UpdateItemTask().execute(compra);
    }

    public void deleteItem(int id) {
        new DeleteItemTask().execute(id);
    }

    class UpdateItemTask extends AsyncTask<Compra, Void, Void> {
        AppDatabase db;
        @Override
        protected Void doInBackground(Compra... compras) {
            if(compras==null || compras.length==0) return null;
            try {
                Compra c = compras[0];
                db = Room.databaseBuilder(MainActivity.this, AppDatabase.class, "monkey_db").build();
                db.compraDao().updateItem(c.id, c.custo, c.categoria, c.detalhes, c.dia, c.mes, c.ano);
            }catch (Exception e){
                Log.d("db", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try{
                if(db!=null && db.isOpen()) db.close();
                new UpdateListViewsTask().execute();
            }catch (Exception e){
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class DeleteItemTask extends AsyncTask<Integer, Void, Void> {
        AppDatabase db;
        @Override
        protected Void doInBackground(Integer... ids) {
            if(ids==null || ids.length==0) return null;
            try {
                int id = ids[0];
                db = Room.databaseBuilder(MainActivity.this, AppDatabase.class, "monkey_db").build();
                db.compraDao().deleteItem(id);
            }catch (Exception e){
                Log.d("db", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try{
                if(db!=null && db.isOpen()) db.close();
                new UpdateListViewsTask().execute();
            }catch (Exception e){
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class InsertTask extends AsyncTask<Compra, Void, Void> {
        AppDatabase db;
        @Override
        protected Void doInBackground(Compra... compras) {
            try {
                db = Room.databaseBuilder(MainActivity.this, AppDatabase.class, "monkey_db").build();
                db.compraDao().insertAll(compras);
            }catch (Exception e){
                Log.d("db", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try{
                if(db!=null && db.isOpen()) db.close();
                new UpdateListViewsTask().execute();
            }catch (Exception e){
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class Aux2List{
        public List<Compra> lc;
        public List<MonkeyAverage> la;

        public Aux2List(List<Compra> lc, List<MonkeyAverage> la) {
            this.lc = lc;
            this.la = la;
        }
    }
    class UpdateListViewsTask extends AsyncTask<Void, Void, Aux2List> {
        AppDatabase db;
        @Override
        protected Aux2List doInBackground(Void... aVoid) {
            try {
                db = Room.databaseBuilder(MainActivity.this, AppDatabase.class, "monkey_db").build();
                if(ano>-1){
                    if(mes>-1 && mes<12){
                        List<Compra> lc = db.compraDao().getAll(mes, ano);
                        List<MonkeyAverage> la = db.monkeyAverageDAO().getSum(mes, ano);
                        MonkeyAverage ma = new MonkeyAverage();
                        ma.custo = db.monkeyAverageDAO().getTotalSum(mes, ano);
                        ma.categoria = "Total";
                        la.add(ma);
                        return new Aux2List(lc, la);
                    }
                    List<Compra> lc = db.compraDao().getAll(ano);
                    List<MonkeyAverage> la = db.monkeyAverageDAO().getSum(ano);
                    MonkeyAverage ma = new MonkeyAverage();
                    ma.custo = db.monkeyAverageDAO().getTotalSum(ano);
                    ma.categoria = "Total";
                    la.add(ma);
                    return new Aux2List(lc, la);
                }
                List<Compra> lc = db.compraDao().getAll();
                List<MonkeyAverage> la = db.monkeyAverageDAO().getSum();
                MonkeyAverage ma = new MonkeyAverage();
                ma.custo = db.monkeyAverageDAO().getTotalSum();
                ma.categoria = "Total";
                la.add(ma);
                return new Aux2List(lc, la);
            }catch (Exception e){
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Aux2List lists) {
            try {
                monkeyDetailsFragment.updateList(lists.lc);
                monkeysFragment.updateList(lists.la);
                if(db!=null && db.isOpen()) db.close();
            }catch (Exception e){
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    //!Database!
}
