package com.stolyarov.bogdan.lentaru.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stolyarov.bogdan.lentaru.R;
import com.stolyarov.bogdan.lentaru.model.NewsListModel;
import com.stolyarov.bogdan.lentaru.model.NewsModel;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Bagi on 11.11.2014.
 */
public class NewsListAdapter extends BaseAdapter {


    private final Activity context;

    private static ArrayList<NewsListModel> listNews;

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
        if (listNews != null) {
            NewsListModel newsListModel = listNews.get(i);
            viewHolder.title.setText(newsListModel.getTitle());
            viewHolder.category.setText(newsListModel.getCategory());
            viewHolder.publicDate.setText(newsListModel.getPublicDate());
        }
        return newsView;
    }

    @Override
    public int getCount() {
        return listNews.size();

    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public NewsListAdapter(Activity context, ArrayList<NewsListModel> newsListModels) {
        listNews = newsListModels;
        this.context = context;
    }
}
