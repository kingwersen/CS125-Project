package com.ingwersen.kyle.cs125_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
{

    private BottomNavigationView mNavView;
    private ViewPager mViewPager;
    private ListView mListViewSuggest;

    private ArrayList<StoreItem> mStoreItems;
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavView = (BottomNavigationView) findViewById(R.id.navigation);
        mNavView.setSelectedItemId(R.id.navigation_suggest);
        mNavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new CustomPagerAdapter(this));
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);

        //list = new ArrayList<String>(Arrays.asList("111,222,333,444,555,666".split(",")));
        //mListViewSuggest = (ListView) findViewById(R.id.listview_suggest);
        //mListViewSuggest.setAdapter(new CustomListAdapter(list, this) );

        // TODO:
        // 0. Finish StoreItem
        // 1. Add Filter Capabilities
        // 2. Voice Input: https://developer.android.com/training/wearables/apps/voice.html
        // 3. Image Input
        // 4. Build Recommender
    }

    private void loadStoreItems()
    {
        File path = new File(getFilesDir(), getString(R.string.save_path));
        mStoreItems = StoreItem.loadList(path);
    }

    private void saveStoreItems()
    {
        File path = new File(getFilesDir(), getString(R.string.save_path));
        StoreItem.saveList(path, mStoreItems);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            switch (item.getItemId())
            {
                case R.id.navigation_suggest:
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.navigation_list:
                    mViewPager.setCurrentItem(1);
                    break;
                case R.id.navigation_history:
                    mViewPager.setCurrentItem(2);
                    break;
                default:
                    return false;
            }
            return true;
        }
    };

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener()
    {
        int mPrevPage = 0;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {

        }

        @Override
        public void onPageSelected(int position)
        {
            mNavView.getMenu().getItem(mPrevPage).setChecked(false);
            mNavView.getMenu().getItem(position).setChecked(true);
            mPrevPage = position;
        }

        @Override
        public void onPageScrollStateChanged(int state)
        {

        }
    };

}
