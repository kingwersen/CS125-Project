package com.ingwersen.kyle.cs125_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ingwersen.kyle.cs125_project.dummy.DummyContent;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements FragmentBase.OnListFragmentInteractionListener
{
    private FragmentPagerAdapter mFragmentPagerAdapter;

    private BottomNavigationView mNavView;
    private ViewPager mViewPager;
    private RecyclerView mSuggestRecylerView;

    private ArrayList<StoreItem> mStoreItems;
    private String[] list;
    private EditText filterBox;
    private Filter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavView = (BottomNavigationView) findViewById(R.id.navigation);
        mNavView.setSelectedItemId(R.id.navigation_suggest);
        mNavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mFragmentPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), this, mStoreItems);
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);

        filter = new Filter();
        filterBox = (EditText) findViewById(R.id.filter_box);
        Button filterButton = (Button) findViewById(R.id.filter_button);
        filterButton.setOnClickListener(mOnClickListener);

        loadStoreItems();


        // TODO:
        // 0. Finish StoreItem
        // 1. Finish Filter Capabilities
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

    private void applyFilter()
    {
        filter.apply(filterBox.getText().toString(), mStoreItems);
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

    private View.OnClickListener mOnClickListener = new  View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            applyFilter();
        }
    };

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item)
    {

    }

    private static class MyPagerAdapter extends FragmentPagerAdapter
    {
        // https://github.com/codepath/android_guides/wiki/ViewPager-with-FragmentPagerAdapter
        private static int NUM_ITEMS = 3;

        private Context mContext;

        private ArrayList<StoreItem> mStoreItems;

        public MyPagerAdapter(FragmentManager fragmentManager, Context context, ArrayList<StoreItem> storeItems) {
            super(fragmentManager);
            mContext = context;
            mStoreItems = storeItems;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position) {
                case 0:
                    return FragmentSuggest.newInstance(mStoreItems);
                case 1:
                    return FragmentCart.newInstance(mStoreItems);
                case 2:
                    return FragmentHistory.newInstance(mStoreItems);
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            TabsObject customPagerEnum = TabsObject.values()[position];
            return mContext.getString(customPagerEnum.getTitleResId());
        }
    }


}
