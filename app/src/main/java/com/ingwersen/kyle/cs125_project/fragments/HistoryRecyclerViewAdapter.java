package com.ingwersen.kyle.cs125_project.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingwersen.kyle.cs125_project.MainActivity;
import com.ingwersen.kyle.cs125_project.R;
import com.ingwersen.kyle.cs125_project.Util;
import com.ingwersen.kyle.cs125_project.model.DataModel.DataListItem;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DataListItem} and makes a call to the
 * specified {@link HistoryFragment.OnHistoryFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder>
{

    private final HistoryListFilter mFilter;
    private final List<DataListItem> mValues;
    private final HistoryFragment.OnHistoryFragmentInteractionListener mListener;

    public HistoryRecyclerViewAdapter(List<DataListItem> items, HistoryFragment.OnHistoryFragmentInteractionListener listener, MainActivity parent)
    {
        mFilter = new HistoryListFilter(items);
        mFilter.setAdapter(this);
        parent.addListFilter(mFilter);
        mValues = mFilter.getReference();
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).name);
        holder.mDateView.setText(Util.formatTime(Duration.between(mValues.get(position).timeLast, Util.currentTime())));

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (null != mListener)
                {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onHistoryFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View mView;
        public final TextView mContentView;
        public final TextView mDateView;
        public DataListItem mItem;

        public ViewHolder(View view)
        {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.name);
            mDateView = (TextView) view.findViewById(R.id.date);
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
