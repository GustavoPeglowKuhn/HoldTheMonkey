package com.alemao.holdthemonkey.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alemao.holdthemonkey.R;
import com.alemao.holdthemonkey.adapter.MonkeyDetailListItemAdapter;
import com.alemao.holdthemonkey.database.AppDatabase;
import com.alemao.holdthemonkey.database.Compra;

import java.util.ArrayList;
import java.util.List;

public class MonkeyListDetailsFragment extends Fragment {

    private ListView listView;
    private ArrayList<Compra> monkeys;
    private MonkeyDetailListItemAdapter adapter;

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return view;
    }

    public void updateList(List<Compra> list){
        monkeys.clear();

        if(list!=null) {
            //monkeys.addAll(list);

            for (Compra item : list) {
                monkeys.add(item);
            }
        }

        adapter.notifyDataSetChanged();
    }
}
