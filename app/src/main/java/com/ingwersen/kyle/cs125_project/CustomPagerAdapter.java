package com.ingwersen.kyle.cs125_project;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kyle on 2/27/2018.
 * https://www.journaldev.com/10096/android-viewpager-example-tutorial
 * Used to construct "swipable" tabs in MainActivity.
 */

public class CustomPagerAdapter extends PagerAdapter
{
    private String[] mList;

    private Context mContext;

    public CustomPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        TabsObject tabsObject = TabsObject.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(tabsObject.getLayoutResId(), collection, false);
        collection.addView(layout);

        System.out.println("Adding List: " + position);
        mList = "111,222,333,444,555,666".split(",");
        RecyclerView list = (RecyclerView) layout.findViewById(R.id.itemlist);
        list.setAdapter(new CustomRecyclerViewAdapter(mList));

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return TabsObject.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        TabsObject customPagerEnum = TabsObject.values()[position];
        return mContext.getString(customPagerEnum.getTitleResId());
    }
}