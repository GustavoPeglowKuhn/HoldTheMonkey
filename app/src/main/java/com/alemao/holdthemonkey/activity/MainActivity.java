package com.alemao.holdthemonkey.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            monkeysFragment.updateList();
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
