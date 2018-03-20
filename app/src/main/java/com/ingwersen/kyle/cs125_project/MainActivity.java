package com.ingwersen.kyle.cs125_project;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;

import com.ingwersen.kyle.cs125_project.fragments.CartFragment;
import com.ingwersen.kyle.cs125_project.fragments.HistoryFragment;
import com.ingwersen.kyle.cs125_project.fragments.SuggestFragment;
import com.ingwersen.kyle.cs125_project.location.GooglePlacesAPI;
import com.ingwersen.kyle.cs125_project.model.DataModel;
import com.ingwersen.kyle.cs125_project.model.DataModel.DataListItem;
import com.ingwersen.kyle.cs125_project.model.DataUtility;
import com.ingwersen.kyle.cs125_project.location.LocationManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        SuggestFragment.OnSuggestFragmentInteractionListener,
        CartFragment.OnCartFragmentInteractionListener,
        HistoryFragment.OnHistoryFragmentInteractionListener
{
    private MainPagerAdapter mFragmentPagerAdapter;

    private Toolbar mToolbar;
    private BottomNavigationView mNavView;
    private ViewPager mViewPager;
    private RecyclerView mSuggestRecylerView;
    private EditText filterBox;

    private List<ListFilter<DataListItem>> mFilters;

    private Location mLastLocation;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataUtility.setContext(this);
        DataModel.init(this);

        // Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Navigation Bar
        mNavView = (BottomNavigationView) findViewById(R.id.navigation);
        mNavView.setSelectedItemId(R.id.navigation_suggest);
        mNavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Pages
        mFilters = new ArrayList<ListFilter<DataListItem>>();
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mFragmentPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        mViewPager.setOffscreenPageLimit(MainPagerAdapter.NUM_ITEMS - 1);  // Don't delete pages.
        mViewPager.setPageMargin(20);
        //mViewPager.setPageMarginDrawable(R.color.colorPrimaryDark);

        // Filters
        filterBox = (EditText) findViewById(R.id.filter_box);
        filterBox.addTextChangedListener(mOnFilterChangeListener);
        Button filterButton = (Button) findViewById(R.id.filter_button);
        filterButton.setOnClickListener(mOnFilterButtonListener);
        updateListFilters();

        // Location Manager
        LocationManager.start(this, mLocationChangedListener);

        setActionBar(0);


        // TODO:
        // 2. Voice Input: https://developer.android.com/training/wearables/apps/voice.html
        // 3. Image Input
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
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
                    setActionBar(0);
                    break;
                case R.id.navigation_cart:
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

    private LocationManager.LocationChangedListener mLocationChangedListener = new LocationManager.LocationChangedListener()
    {
        @Override
        public void onLocationChanged(Location location)
        {
            // TODO: Fix web error.
            System.out.println("Location Changed: " + location);
            //System.out.println("Places: " + GooglePlacesAPI.getPlaces(location));
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
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorCart2)));
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
        item.incrementUser();
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
