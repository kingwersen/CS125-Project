package com.ingwersen.kyle.cs125_project.fragments;

import com.ingwersen.kyle.cs125_project.ListFilter;
import com.ingwersen.kyle.cs125_project.model.DataModel.DataListItem;

import java.util.List;

/**
 * Created by kyle on 3/9/2018.
 */

public class CartListFilter extends ListFilter<DataListItem>
{
    private CartRecyclerViewAdapter mAdapter;

    public CartListFilter(List<DataListItem> items)
    {
        super(items);
    }

    public void setAdapter(CartRecyclerViewAdapter adapter)
    {
        mAdapter = adapter;
    }

    @Override
    public void update()
    {
        mOutput.clear();
        for (DataListItem item : mValues)
        {
            if (item.state == DataListItem.DataItemState.IN_CART &&
                    (mFilter.size() == 0 || mFilter.contains(item.name.toLowerCase())))
            {
                mOutput.add(item);
            }
        }
        if (mAdapter != null)
        {
            mAdapter.notifyDataSetChanged();
        }
    }
}
