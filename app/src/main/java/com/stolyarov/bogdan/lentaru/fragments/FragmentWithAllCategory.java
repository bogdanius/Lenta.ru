package com.stolyarov.bogdan.lentaru.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.stolyarov.bogdan.lentaru.R;
import com.stolyarov.bogdan.lentaru.adapter.ItemAdapter;
import com.stolyarov.bogdan.lentaru.model.Item;

import java.util.ArrayList;

/**
 * Created by Bagi on 02.12.2014.
 */

public class FragmentWithAllCategory extends Fragment {

    private ArrayList<Item> items;
    private ListView newsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_category, container, false);
        newsList = (ListView) view.findViewById(R.id.news_list_with_all_category);


        ItemAdapter adapter = new ItemAdapter(getActivity(), items);
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(adapter);
        alphaInAnimationAdapter.setAbsListView(newsList);
        newsList.setAdapter(alphaInAnimationAdapter);

        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentOneNewsMobile fragmentOneNewsMobile = new FragmentOneNewsMobile();
                fragmentOneNewsMobile.setItem(items.get(position));
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (getResources().getBoolean(R.bool.portrait_only)) {
                    fragmentTransaction.replace(R.id.container_for_list, fragmentOneNewsMobile);
                } else {
                    fragmentTransaction.replace(R.id.container, fragmentOneNewsMobile);
                }
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}
