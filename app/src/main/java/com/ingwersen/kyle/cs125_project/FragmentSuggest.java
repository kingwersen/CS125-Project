package com.ingwersen.kyle.cs125_project;

import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by kyle on 3/8/2018.
 */

public class FragmentSuggest extends FragmentListBase<StoreItem>
{

    public FragmentSuggest()
    {
    }

    public static FragmentSuggest newInstance(ArrayList<StoreItem> storeItems)
    {
        FragmentSuggest fragment = new FragmentSuggest();
        fragment.setArguments(fragment.args(storeItems));
        return fragment;
    }

    @Override
    public void updateList()
    {
    }

}
