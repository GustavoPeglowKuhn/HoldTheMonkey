package com.alemao.holdthemonkey.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alemao.holdthemonkey.R;
import com.alemao.holdthemonkey.adapter.MonkeyListItemAdapter;
import com.alemao.holdthemonkey.database.AppDatabase;
import com.alemao.holdthemonkey.database.Compra;
import com.alemao.holdthemonkey.database.MonkeyAverage;
import com.alemao.holdthemonkey.model.MonkeyListItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonkeyListFragment extends Fragment {

    private ListView listView;
    private ArrayList<MonkeyListItem> monkeys;
    private MonkeyListItemAdapter adapter;

    private AppDatabase db; //static

    public MonkeyListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monkey_list, container, false);

        listView = view.findViewById(R.id.fml_lv_categories);
        monkeys = new ArrayList<>();

        adapter = new MonkeyListItemAdapter(getActivity(), monkeys);
        listView.setAdapter(adapter);

        /*listView.setOnClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/

        updateList(0,0);

        return view;
    }

    class GetAllTask extends AsyncTask<Integer, Void, List<MonkeyAverage>> {

        /*
        * ints:
        * [0] = tipo de busca
        * [1] = ano
        * [2] = mes
        * */

        @Override
        protected List<MonkeyAverage> doInBackground(Integer... ints) {
            try {
                if(ints != null){
                    if(ints[0] == 0){
                        return db.getDb().monkeyAverageDAO().getAll();
                    }else if(ints[0] == 1){
                        return db.getDb().monkeyAverageDAO().getSum();
                    }else if(ints[0] == 2){
                        return db.getDb().monkeyAverageDAO().getAll(ints[1], ints[2]);
                    }else if(ints[0] == 3){
                        return db.getDb().monkeyAverageDAO().getSum(ints[1], ints[2]);
                    }
                }
            }catch (Exception e){
                Log.d("db", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<MonkeyAverage> list) {
            monkeys.clear();

            for(MonkeyAverage item : list){
                monkeys.add(new MonkeyListItem(item.categoria, item.custo));
            }

            adapter.notifyDataSetChanged();
        }
    }

    public void updateList(int sortType, int mes){
        new GetAllTask().execute(sortType, Calendar.getInstance().get(Calendar.YEAR), mes);
    }
}
