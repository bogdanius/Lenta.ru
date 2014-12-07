package com.stolyarov.bogdan.lentaru.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.stolyarov.bogdan.lentaru.R;
import com.stolyarov.bogdan.lentaru.model.Item;

/**
 * Created by Bagi on 28.11.2014.
 */
public class FragmentOneNewsMobile extends Fragment {

    private Item item;
    private ImageView imageView;
    private String url;
    ImageLoader imageLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_news, container, false);

        TextView date = (TextView) view.findViewById(R.id.one_news_date);
        TextView title = (TextView) view.findViewById(R.id.one_news_title);
        TextView description = (TextView) view.findViewById(R.id.one_news_description);
        TextView category = (TextView) view.findViewById(R.id.one_news_category);
        imageView = (ImageView) view.findViewById(R.id.one_news_image);

        url = item.getImageUrl();
        date.setText(item.getPubDate());
        title.setText(item.getTitle());
        description.setText(item.getDescription());
        category.setText(item.getCategory());
        imageLoad();
        return view;
    }

    public void imageLoad() {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
                .threadPoolSize(5)
                .threadPriority(Thread.MIN_PRIORITY + 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .imageDownloader(new BaseImageDownloader(getActivity()))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .build();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .build();

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        imageLoader.displayImage(url, imageView, options);
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
