package com.alemao.holdthemonkey.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alemao.holdthemonkey.R;
import com.alemao.holdthemonkey.adapter.MonkeyDetailListItemAdapter;
import com.alemao.holdthemonkey.adapter.MonkeyListItemAdapter;
import com.alemao.holdthemonkey.database.AppDatabase;
import com.alemao.holdthemonkey.database.Compra;
import com.alemao.holdthemonkey.database.MonkeyAverage;
import com.alemao.holdthemonkey.model.MonkeyListItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonkeyListDetailsFragment extends Fragment {

    private ListView listView;
    private ArrayList<Compra> monkeys;
    private MonkeyDetailListItemAdapter adapter;

    private AppDatabase db; //static

    public MonkeyListDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monkey_list_details, container, false);

        listView = view.findViewById(R.id.fmld_lv_categories);
        monkeys = new ArrayList<>();

        adapter = new MonkeyDetailListItemAdapter(getActivity(), monkeys);
        listView.setAdapter(adapter);

        /*listView.setOnClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/

        return view;
    }

    class UpdateTask extends AsyncTask<Integer, Void, List<Compra>> {
        //ints
        //[0] = mes  (-1 == sem mes)
        //[1] = ano

        @Override
        protected List<Compra> doInBackground(Integer... ints) {
            try {
                if(ints != null){
                    if (ints[0] == -1) //sem mes especificado
                        return db.getDb().compraDao().getAll();
                    else
                        return db.getDb().compraDao().getAll(ints[0], ints[1]);
                }
                return db.getDb().compraDao().getAll();
            }catch (Exception e){
                Log.d("db", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Compra> list) {
            monkeys.clear();

            if(list!=null) {
                for (Compra item : list) {
                    monkeys.add(item);
                }
            }

            adapter.notifyDataSetChanged();
        }
    }

    public void updateList(int mes, int ano){
        new UpdateTask().execute(mes, ano);
    }
}
