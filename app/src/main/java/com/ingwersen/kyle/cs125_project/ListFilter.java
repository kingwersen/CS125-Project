package com.ingwersen.kyle.cs125_project;

import android.gesture.Prediction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kyle on 3/9/2018.
 */

public abstract class ListFilter<T>
{
    protected final List<T> mValues;
    protected final List<T> mOutput;
    protected List<String> mFilter;

    public ListFilter(List<T> items)
    {
        mValues = items;
        mOutput = new ArrayList<>(items);
        mFilter = new ArrayList<>();
        update();
    }

    public List<T> getReference()
    {
        return mOutput;
    }

    public void applyFilter(String filter)
    {
        mFilter = new ArrayList<>(Arrays.asList(filter.toLowerCase().split(" ")));
        mFilter.removeIf(item -> item == null || "".equals(item));
        update();
    }

    public void clearFilter()
    {
        applyFilter("");
    }

    /**
     *  Clear the list of items and only show ones that match the filter.
     */
    public abstract void update();

}
