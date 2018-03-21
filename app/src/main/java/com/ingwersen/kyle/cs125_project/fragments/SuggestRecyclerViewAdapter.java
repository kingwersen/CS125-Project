package com.ingwersen.kyle.cs125_project.fragments;

import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingwersen.kyle.cs125_project.MainActivity;
import com.ingwersen.kyle.cs125_project.R;
import com.ingwersen.kyle.cs125_project.Util;
import com.ingwersen.kyle.cs125_project.model.DataModel.DataListItem;
import com.ingwersen.kyle.cs125_project.model.DataUtility;

import org.w3c.dom.Text;

import java.time.Duration;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DataListItem} and makes a call to the
 * specified {@link SuggestFragment.OnSuggestFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SuggestRecyclerViewAdapter extends RecyclerView.Adapter<SuggestRecyclerViewAdapter.ViewHolder>
{

    private final SuggestListFilter mFilter;
    private final List<DataListItem> mValues;
    private final SuggestFragment.OnSuggestFragmentInteractionListener mListener;

    public SuggestRecyclerViewAdapter(List<DataListItem> items, SuggestFragment.OnSuggestFragmentInteractionListener listener, MainActivity parent)
    {
        mFilter = new SuggestListFilter(items);
        mFilter.setAdapter(this);
        parent.addListFilter(mFilter);
        mValues = mFilter.getReference();
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_utility, parent, false);
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
        holder.mUtilityView.setText(String.format("%.2f", item.userUtility));

        holder.mUtility1.setText(String.format("%.2f", item.util1));
        holder.mUtility2.setText(String.format("%.2f", item.util2));
        holder.mUtility3.setText(String.format("%.2f", item.util3));
        holder.mExpect2.setText(item.totalCount > 1 ? Util.formatTime(Duration.ofSeconds((long) item.totalMean)) : "N/A");

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (null != mListener)
                {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onSuggestFragmentInteraction(holder.mItem);
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
        public final TextView mUtilityView;

        public final TextView mUtility1;
        public final TextView mUtility2;
        public final TextView mUtility3;
        public final TextView mExpect2;

        public DataListItem mItem;

        public ViewHolder(View view)
        {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.item_name);
            mExpectView = (TextView) view.findViewById(R.id.item_exp);
            mLastView = (TextView) view.findViewById(R.id.item_last);
            mUtilityView = (TextView) view.findViewById(R.id.item_utility);

            mUtility1 = (TextView) view.findViewById(R.id.item_util1);
            mUtility2 = (TextView) view.findViewById(R.id.item_util2);
            mUtility3 = (TextView) view.findViewById(R.id.item_util3);
            mExpect2 = (TextView) view.findViewById(R.id.item_exp2);
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
