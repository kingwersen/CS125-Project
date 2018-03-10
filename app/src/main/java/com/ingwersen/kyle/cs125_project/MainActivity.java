package com.ingwersen.kyle.cs125_project;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ingwersen.kyle.cs125_project.fragments.CartFragment;
import com.ingwersen.kyle.cs125_project.fragments.HistoryFragment;
import com.ingwersen.kyle.cs125_project.fragments.SuggestFragment;
import com.ingwersen.kyle.cs125_project.model.DataModel;
import com.ingwersen.kyle.cs125_project.model.DataModel.DataListItem;
import com.ingwersen.kyle.cs125_project.model.DataUtility;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        SuggestFragment.OnSuggestFragmentInteractionListener,
        CartFragment.OnCartFragmentInteractionListener,
        HistoryFragment.OnHistoryFragmentInteractionListener
{
    private MainPagerAdapter mFragmentPagerAdapter;

    private BottomNavigationView mNavView;
    private ViewPager mViewPager;
    private RecyclerView mSuggestRecylerView;
    private EditText filterBox;

    private List<ListFilter<DataListItem>> mFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavView = (BottomNavigationView) findViewById(R.id.navigation);
        mNavView.setSelectedItemId(R.id.navigation_suggest);
        mNavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mFilters = new ArrayList<ListFilter<DataListItem>>();

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mFragmentPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        mViewPager.setOffscreenPageLimit(MainPagerAdapter.NUM_ITEMS - 1);  // Don't delete pages.
        mViewPager.setPageMargin(20);
        //mViewPager.setPageMarginDrawable(R.color.colorPrimaryDark);

        filterBox = (EditText) findViewById(R.id.filter_box);
        filterBox.addTextChangedListener(mOnFilterChangeListener);
        Button filterButton = (Button) findViewById(R.id.filter_button);
        filterButton.setOnClickListener(mOnFilterButtonListener);


        loadStoreItems();
        System.out.println("APPLICATION START");
        setActionBar(0);


        // TODO:
        // 0. Finish StoreItem
        // 1. Finish Filter Capabilities
        // 2. Voice Input: https://developer.android.com/training/wearables/apps/voice.html
        // 3. Image Input
        // 4. Build Recommender
    }

    private void loadStoreItems()
    {
        // TODO: Use DataListItems
        //File path = new File(getFilesDir(), getString(R.string.save_path));
        //mStoreItems = StoreItem.loadList(path);
    }

    private void saveStoreItems()
    {
        // TODO: Use DataListItems
        //File path = new File(getFilesDir(), getString(R.string.save_path));
        //StoreItem.saveList(path, mStoreItems);
    }

    // TODO: USE DataListItems
    //private void applyFilter()
    //{
    //    filter.apply(filterBox.getText().toString(), mStoreItems);
    //}

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
                    setActionBar(0);
                    break;
                case R.id.navigation_list:
                    mViewPager.setCurrentItem(1);
                    setActionBar(1);
                    break;
                case R.id.navigation_history:
                    mViewPager.setCurrentItem(2);
                    setActionBar(2);
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
            setActionBar(position);
        }

        @Override
        public void onPageScrollStateChanged(int state)
        {

        }
    };

    private void setActionBar(int page)
    {
        switch (page)
        {
            case 0:
                getSupportActionBar().setTitle(R.string.title_suggest);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorSuggest)));
                break;
            case 1:
                getSupportActionBar().setTitle(R.string.title_cart);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorCart)));
                break;
            case 2:
                getSupportActionBar().setTitle(R.string.title_history);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorHistory)));
                break;
        }
    }

    private TextWatcher mOnFilterChangeListener = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
            updateListFilters();
        }

        @Override
        public void afterTextChanged(Editable editable)
        {

        }
    };

    private View.OnClickListener mOnFilterButtonListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            updateListFilters();
        }
    };

    public void addListFilter(ListFilter<DataListItem> list)
    {
        mFilters.add(list);
    }

    @Override
    public void onSuggestFragmentInteraction(DataListItem item)
    {
        // Move Item to Cart
        item.state = DataListItem.DataItemState.IN_CART;
        updateListFilters();
    }

    @Override
    public void onCartFragmentInteraction(DataListItem item)
    {
        // Move Item to Suggestions
        item.state = DataListItem.DataItemState.SUGGESTED;
        item.increment();
        updateListFilters();
    }

    @Override
    public void onHistoryFragmentInteraction(DataListItem item)
    {
        // Move Item to Cart
        item.state = DataListItem.DataItemState.IN_CART;
        updateListFilters();
    }

    public void updateListFilters()
    {
        DataUtility.updateUtility(DataModel.ITEMS);
        for (ListFilter<DataListItem> filter : mFilters)
        {
            // Apply the current filter and update the list.
            filter.applyFilter(filterBox.getText().toString());
        }
    }

    private static class MainPagerAdapter extends FragmentPagerAdapter
    {
        // https://github.com/codepath/android_guides/wiki/ViewPager-with-FragmentPagerAdapter
        private static int NUM_ITEMS = 3;

        private Context mContext;

        public MainPagerAdapter(FragmentManager fragmentManager, Context context) {
            super(fragmentManager);
            mContext = context;
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
                    return SuggestFragment.newInstance(1, (MainActivity) mContext);
                case 1:
                    return CartFragment.newInstance(1, (MainActivity) mContext);
                case 2:
                    return HistoryFragment.newInstance(1, (MainActivity) mContext);
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            Navigation customPagerEnum = Navigation.values()[position];
            return mContext.getString(customPagerEnum.getTitleResId());
        }
    }


}
