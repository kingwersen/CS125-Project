package com.ingwersen.kyle.cs125_project;

import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;

/**
 * Created by kyle on 3/8/2018.
 */

public class FragmentSuggest extends FragmentListBase
{


    ArrayAdapter<StoreItem> mStoreItemAdapter;

    public FragmentSuggest()
    {
    }

    public static FragmentSuggest newInstance(ArrayList<StoreItem> storeItems)
    {
        return FragmentBase.newInstance();
    }

    @Override
    public void updateList()
    {
    }

}
