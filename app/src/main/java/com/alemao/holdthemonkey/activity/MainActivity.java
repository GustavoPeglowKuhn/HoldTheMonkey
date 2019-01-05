package com.alemao.holdthemonkey.activity;

import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.alemao.holdthemonkey.R;
import com.alemao.holdthemonkey.adapter.MonkeyDetailListItemAdapter;
import com.alemao.holdthemonkey.database.AppDatabase;
import com.alemao.holdthemonkey.database.Compra;
import com.alemao.holdthemonkey.database.MonkeyAverage;
import com.alemao.holdthemonkey.fragment.MonkeyListDetailsFragment;
import com.alemao.holdthemonkey.fragment.MonkeyListFragment;
import com.alemao.holdthemonkey.helper.SlidingTabLayout;
import com.alemao.holdthemonkey.model.MonkeyListItem;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    //tab titles names
    String[] tabsTitles = {"TOODS", "POR CATEGORIA"};
    MonkeyListFragment monkeysFragment;
    MonkeyListDetailsFragment monkeyDetailsFragment;

    static int mes;     //de 0 a 11
    static int ano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
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

        mes = Calendar.getInstance().get(Calendar.MONTH);
        ano = Calendar.getInstance().get(Calendar.YEAR);

        new UpdateAllTask().execute();
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
            Toast.makeText(MainActivity.this, R.string.version_name, Toast.LENGTH_SHORT).show();
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

                Compra c = new Compra(categoria, custo, detalhes);

                new InsertTask().execute(c);
            }
        });

        alertDialog.show();
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
                new UpdateAllTask().execute();
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
    class UpdateAllTask extends AsyncTask<Void, Void, Aux2List> {
        AppDatabase db;
        @Override
        protected Aux2List doInBackground(Void... aVoid) {
            try {
                db = Room.databaseBuilder(MainActivity.this, AppDatabase.class, "monkey_db").build();
                if (mes>-1 && mes<12) return new Aux2List(db.compraDao().getAll(mes, ano), db.monkeyAverageDAO().getSum(mes, ano));
                return new Aux2List(db.compraDao().getAll(), db.monkeyAverageDAO().getSum());
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

    /*class UpdateDetailsTask extends AsyncTask<Void, Void, List<Compra>> {
        AppDatabase db;
        @Override
        protected List<Compra> doInBackground(Void... aVoid) {
            try {
                db = Room.databaseBuilder(MainActivity.this, AppDatabase.class, "monkey_db").build();
                if (mes>-1 && mes<12)
                    return db.compraDao().getAll(mes, ano);
                return db.compraDao().getAll();
            }catch (Exception e){
                Log.d("db", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Compra> list) {
            monkeyDetailsFragment.updateList(list);
            if(db!=null && db.isOpen()) db.close();
        }
    }

    class UpdateCategoriesTask extends AsyncTask<Void, Void, List<MonkeyAverage>> {
        AppDatabase db;
        @Override
        protected List<MonkeyAverage> doInBackground(Void... aVoid) {
            try {
                db = Room.databaseBuilder(MainActivity.this, AppDatabase.class, "monkey_db").build();
                if (mes>-1 && mes<12)
                    return db.monkeyAverageDAO().getSum(mes, ano);
                return db.monkeyAverageDAO().getSum();
            }catch (Exception e){
                Log.d("db", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<MonkeyAverage> list) {
            monkeysFragment.updateList(list);
            if(db!=null && db.isOpen()) db.close();
        }
    }*/
}
