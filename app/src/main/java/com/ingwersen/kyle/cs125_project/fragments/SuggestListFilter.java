package com.ingwersen.kyle.cs125_project.fragments;

import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.ingwersen.kyle.cs125_project.ListFilter;
import com.ingwersen.kyle.cs125_project.model.DataModel.DataListItem;

import java.util.List;

/**
 * Created by kyle on 3/9/2018.
 */

public class SuggestListFilter extends ListFilter<DataListItem>
{

    private SuggestRecyclerViewAdapter mAdapter;

    public SuggestListFilter(List<DataListItem> items)
    {
        super(items);
    }

    public void setAdapter(SuggestRecyclerViewAdapter adapter)
    {
        mAdapter = adapter;
    }

    @Override
    public void update()
    {
        System.out.println("UPDATE SUGGEST VIEW");
        System.out.println(mFilter.toString());
        mOutput.clear();
        for (DataListItem item : mValues)
        {
            if (item.state == DataListItem.DataItemState.SUGGESTED &&
                    (mFilter.size() == 0 || mFilter.contains(item.name.toLowerCase())))
            {
                mOutput.add(item);
            }
        }
        if (mOutput.size() > 1)
        {
            mOutput.sort((left, right) -> left.utility < right.utility ? 1 : (left.utility > right.utility ? -1 : 0));
        }
        if (mAdapter != null)
        {
            mAdapter.notifyDataSetChanged();
        }
    }
}
