package com.ingwersen.kyle.cs125_project.fragments;

import com.ingwersen.kyle.cs125_project.ListFilter;
import com.ingwersen.kyle.cs125_project.model.DataModel.DataListItem;

import java.util.List;

/**
 * Created by kyle on 3/9/2018.
 */

public class HistoryListFilter extends ListFilter<DataListItem>
{
    private HistoryRecyclerViewAdapter mAdapter;

    public HistoryListFilter(List<DataListItem> items)
    {
        super(items);
    }

    public void setAdapter(HistoryRecyclerViewAdapter adapter)
    {
        mAdapter = adapter;
    }

    @Override
    public void update()
    {
        mOutput.clear();
        for (DataListItem item : mValues)
        {
            if (item.state != DataListItem.DataItemState.HIDDEN
                    && (mFilter.size() == 0 || mFilter.contains(item.name.toLowerCase()))
                    && (item.userCount > 0))
            {
                mOutput.add(item);
            }
        }
        if (mOutput.size() > 1)
        {
            // Reverse history order. Newest -> Oldest.
            mOutput.sort((left, right) -> -left.userLast.compareTo(right.userLast));
        }
        if (mAdapter != null)
        {
            mAdapter.notifyDataSetChanged();
        }
    }
}
