package com.alemao.holdthemonkey.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
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
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.alemao.holdthemonkey.R;
import com.alemao.holdthemonkey.database.AppDatabase;
import com.alemao.holdthemonkey.database.Compra;
import com.alemao.holdthemonkey.fragment.MonkeyListFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;

    //tab titles names
    String[] tabsTitles = {"TOODS"};
    MonkeyListFragment monkeysFragment;

    AppDatabase db;

    /*
    * 0 - todos os itens
    * 1 - todos separados por categorias
    * 2 - todos do mes n
    * 3 - todos do mes n separados por categoria
    * */
    static int sortType = 1;
    static int mes = 1;    //de 0 a 11

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        viewPager = findViewById(R.id.main_vp);

        AppDatabase.setContext(MainActivity.this);
        monkeysFragment = new MonkeyListFragment();

        FragmentStatePagerAdapter tabAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if(position == 0)
                    return monkeysFragment;//chatListFragment;
                return null;
            }

            @Override
            public int getCount() { return tabsTitles.length; }

            @Override
            public CharSequence getPageTitle(int position) { return tabsTitles[position]; }
        };
        viewPager.setAdapter(tabAdapter);

        monkeysFragment.updateList(sortType, mes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addMonkey(View view){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        final View v = inflater.inflate(R.layout.dialog_add_compra, null);

        alertDialog.setView(v);

        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });

        alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText txtCusto = v.findViewById(R.id.dac_edit_custo);
                EditText txtcategoria = v.findViewById(R.id.dac_edit_categoria);

                float custo = Float.parseFloat(txtCusto.getText().toString());
                String categoria = txtcategoria.getText().toString();

                Compra c = new Compra(categoria, custo, "");

                new InsertTask().execute(c);
            }
        });

        alertDialog.show();
    }

    public void sortSelect(View view){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        final View v = inflater.inflate(R.layout.dialog_sort_select, null);

        alertDialog.setView(v);

        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });

        alertDialog.setPositiveButton("Sort", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int auxSortType;
                if(((RadioButton)v.findViewById(R.id.dss_rb0)).isChecked())
                    auxSortType = 0;
                else if(((RadioButton)v.findViewById(R.id.dss_rb1)).isChecked())
                    auxSortType = 1;
                else if(((RadioButton)v.findViewById(R.id.dss_rb2)).isChecked())
                    auxSortType = 2;
                else if(((RadioButton)v.findViewById(R.id.dss_rb3)).isChecked())
                    auxSortType = 3;
                else {
                    Toast.makeText(MainActivity.this, "Selecione um metodo", Toast.LENGTH_SHORT).show();
                    return;
                }

                int month;
                if(auxSortType==2 || auxSortType==3) {
                    try{
                        month = Integer.parseInt(((EditText) v.findViewById(R.id.dss_edit_month)).getText().toString());
                    }catch(Exception e){
                        month = 100;
                    }
                    if(month<1 || month>12) {
                        Toast.makeText(MainActivity.this, "Mes invalido", Toast.LENGTH_SHORT).show();
                    }

                    mes = month-1;
                }
                sortType = auxSortType;
                monkeysFragment.updateList(sortType, mes);
            }
        });
        alertDialog.show();
    }

    class InsertTask extends AsyncTask<Compra, Void, Void> {
        @Override
        protected Void doInBackground(Compra... compras) {
            try {
                db.getDb().userDao().insertAll(compras);
            }catch (Exception e){
                Log.d("db", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            monkeysFragment.updateList(sortType, mes);
        }
    }

    class GetCategoriesTask extends AsyncTask<View, Void, List<String>> {
        View listItems;

        @Override
        protected List<String> doInBackground(View... view) {
            try {
                listItems = view[0];
                return db.getDb().userDao().getAllCategories();
            }catch (Exception e){
                Log.d("db", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> list) {
            for(String s : list){

            }
        }
    }

    class ReadTestTask extends AsyncTask<Void, Void, List<Compra>> {

        @Override
        protected List<Compra> doInBackground(Void... voids) {
            try {
                //return db.getDb().userDao().getSum();
            }catch (Exception e){
                Log.d("db", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Compra> list) {
            //for(Compra compra : list)
            //    Log.d("db", "R$: "+compra.custo);
        }
    }
}
