package com.stolyarov.bogdan.lentaru.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stolyarov.bogdan.lentaru.R;
import com.stolyarov.bogdan.lentaru.model.Item;

/**
 * Created by Bagi on 28.11.2014.
 */
public class FragmentOneNewsMobile extends Fragment {

    Item item;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_news, container, false);

        TextView date = (TextView) view.findViewById(R.id.one_news_date);
        TextView title = (TextView) view.findViewById(R.id.one_news_title);
        TextView description = (TextView) view.findViewById(R.id.one_news_description);
        TextView category = (TextView) view.findViewById(R.id.one_news_category);

        date.setText(item.getPubDate());
        title.setText(item.getTitle());
        description.setText(item.getDescription());
        category.setText(item.getCategory());


        return view;
    }

    public void setItem(Item item){
        this.item = item;
    }
}
