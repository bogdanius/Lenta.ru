package com.stolyarov.bogdan.lentaru.AsyncTasks;

import com.stolyarov.bogdan.lentaru.model.Item;

import java.util.ArrayList;

/**
 * Created by Bagi on 25.11.2014.
 */
public interface OnNewsLoadedComplete {
    public void onNewsLoaded(ArrayList<Item> result);
}
