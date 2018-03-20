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
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DataListItem} and makes a call to the
 * specified {@link CartFragment.OnCartFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder>
{

    private final CartListFilter mFilter;
    private final List<DataListItem> mValues;
    private final CartFragment.OnCartFragmentInteractionListener mListener;

    public CartRecyclerViewAdapter(List<DataListItem> items, CartFragment.OnCartFragmentInteractionListener listener, MainActivity parent)
    {
        mFilter = new CartListFilter(items);
        mFilter.setAdapter(this);
        parent.addListFilter(mFilter);
        mValues = mFilter.getReference();
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        DataListItem item = mValues.get(position);
        holder.mItem = item;
        holder.mNameView.setText(item.name);
        holder.mExpectView.setText(item.userCount > 1 ? Util.formatTime(Duration.ofSeconds((long) item.userMean)) : "N/A");
        holder.mLastView.setText(item.userCount > 1 ? Util.formatTime(Util.timeSince(item.userLast)) : "N/A");
        holder.mLastView.setTextColor(item.getColor().toArgb());

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (null != mListener)
                {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onCartFragmentInteraction(holder.mItem);
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
        public final TextView mNameView;
        public final TextView mExpectView;
        public final TextView mLastView;
        public DataListItem mItem;

        public ViewHolder(View view)
        {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.item_name);
            mExpectView = (TextView) view.findViewById(R.id.item_exp);
            mLastView = (TextView) view.findViewById(R.id.item_last);
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
