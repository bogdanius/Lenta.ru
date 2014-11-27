package com.stolyarov.bogdan.lentaru.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stolyarov.bogdan.lentaru.R;
import com.stolyarov.bogdan.lentaru.model.Item;

import java.util.ArrayList;

/**
 * Created by Bagi on 11.11.2014.
 */
public class ItemAdapter extends BaseAdapter {


    private final Activity context;

    private static ArrayList<Item> items;
    public static final String myLog = "MyLog";

    static class ViewHolder {
        public TextView title;
//        public String link;
        public TextView category;
        public TextView publicDate;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        View newsView = view;

        Log.d(myLog, "GET View");

        if (newsView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            newsView = inflater.inflate(R.layout.one_news_at_list,null,true);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) newsView.findViewById(R.id.news_title_list);
            viewHolder.category = (TextView) newsView.findViewById(R.id.category);
            viewHolder.publicDate = (TextView) newsView.findViewById(R.id.data_list);
            newsView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) newsView.getTag();
        }
        if (items != null) {
            Item item = items.get(i);
            viewHolder.title.setText(item.getTitle());
            viewHolder.category.setText(item.getCategory());
            viewHolder.publicDate.setText(item.getPubDate());
        }
        return newsView;
    }

    @Override
    public int getCount() {
        int count  = 0;
        Log.d(myLog, "items.size in adapter = 0");
        if(items != null) {
            count = items.size();
            Log.d(myLog, "items.size != 0");
        }
        return count;

    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public ItemAdapter(Activity context, ArrayList<Item> items) {
        this.items = items;
        this.context = context;
    }
}
