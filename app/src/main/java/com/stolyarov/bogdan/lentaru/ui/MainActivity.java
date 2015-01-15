package com.stolyarov.bogdan.lentaru.ui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.stolyarov.bogdan.lentaru.R;
import com.stolyarov.bogdan.lentaru.asynctasks.DownloadXmlTask;
import com.stolyarov.bogdan.lentaru.asynctasks.OnNewsLoadedComplete;
import com.stolyarov.bogdan.lentaru.db.DatabaseHelper;
import com.stolyarov.bogdan.lentaru.fragments.FragmentWithAllCategory;
import com.stolyarov.bogdan.lentaru.fragments.FragmentWithCategorySelected;
import com.stolyarov.bogdan.lentaru.model.Item;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ProgressBar progressBar;
    private ArrayList<Item> loadFromDatabaseItems;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] rubricsTitles;
    private static boolean isConnect = false;
    private static ArrayList<Item> items;
    private static boolean loadFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.activity_main);
        initNavigationDrawerViews();
        initViews();
        updateConnectedFlags();
        if (isConnect) {
            updateAndDisplayNews();
        } else {
            loadFromDatabase();
            Toast.makeText(getApplicationContext(), "Check internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private void loadFromDatabase() {
        DatabaseHelper dateBaseHelper = DatabaseHelper.getInstance(this);
        SQLiteDatabase sqLiteDatabase = dateBaseHelper.getWritableDatabase();
        loadFromDatabaseItems = new ArrayList<Item>();
        Item item;
        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.DATABASE_TABLE, new String[]{
                        DatabaseHelper._ID,
                        DatabaseHelper.TITLE_COLUMN,
                        DatabaseHelper.PUBDATE_COLUMN,
                        DatabaseHelper.LINK_COLUMN,
                        DatabaseHelper.DESCRIPTION_COLUMN,
                        DatabaseHelper.CATEGORY_COLUMN,
                        DatabaseHelper.IMAGEURL_COLUMN
                },
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        while (cursor.moveToNext()) {
            item = new Item();
            item.setTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TITLE_COLUMN)));
            item.setPubDate(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.PUBDATE_COLUMN)));
            item.setLink(cursor.getString(cursor.getColumnIndex(DatabaseHelper.LINK_COLUMN)));
            item.setDescription(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DESCRIPTION_COLUMN)));
            item.setCategory(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CATEGORY_COLUMN)));
            item.setImageUrl(cursor.getString(cursor.getColumnIndex(DatabaseHelper.IMAGEURL_COLUMN)));
            loadFromDatabaseItems.add(item);
        }
        cursor.close();
        items = new ArrayList<Item>();
        for (int i = 1; i < loadFromDatabaseItems.size() + 1; i++) {
            items.add(loadFromDatabaseItems.get(loadFromDatabaseItems.size() - i));
        }

        FragmentWithAllCategory fragmentWithAllCategory = new FragmentWithAllCategory();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentWithAllCategory.setItems(items);
        fragmentManager.beginTransaction().replace(R.id.container_for_list, fragmentWithAllCategory).commit();

        progressBar.setVisibility(View.GONE);
        loadFinish = true;
        sqLiteDatabase.close();
        dateBaseHelper.close();
    }

    private void initViews() {
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    // Checks the network connection
    private void updateConnectedFlags() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connectivityManager.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            isConnect = true;
        } else {
            isConnect = false;
        }
    }

    // Uses AsyncTask to download the XML feed from lenta.ru
    public void updateAndDisplayNews() {
        new DownloadXmlTask(this, progressBar, new OnNewsLoadedComplete() {

            @Override
            public void onNewsLoaded(ArrayList<Item> result) {
                items = result;

                FragmentWithAllCategory fragmentWithAllCategory = new FragmentWithAllCategory();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentWithAllCategory.setItems(items);
                fragmentManager.beginTransaction().replace(R.id.container_for_list, fragmentWithAllCategory).commit();
                loadFinish = true;
            }
        }).execute();
    }


    // Update the main content by replacing fragments
    private void selectItem(int position) {

        FragmentWithCategorySelected fragmentWithCategorySelected = new FragmentWithCategorySelected();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentWithCategorySelected.setItems(items);
        fragmentWithCategorySelected.setCategory(rubricsTitles[position]);
        fragmentManager.beginTransaction().replace(R.id.container_for_list, fragmentWithCategorySelected).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(rubricsTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void initNavigationDrawerViews() {
        mTitle = mDrawerTitle = getTitle();
        rubricsTitles = getResources().getStringArray(R.array.rubrics_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, rubricsTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (loadFinish) {
                selectItem(position);
            } else {
                Toast.makeText(getApplicationContext(),"Подождите пока загрузится контент",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}