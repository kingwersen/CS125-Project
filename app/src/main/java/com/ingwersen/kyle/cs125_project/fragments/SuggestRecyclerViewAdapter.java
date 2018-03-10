package com.ingwersen.kyle.cs125_project.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingwersen.kyle.cs125_project.MainActivity;
import com.ingwersen.kyle.cs125_project.R;
import com.ingwersen.kyle.cs125_project.model.DataModel.DataListItem;

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
    private RecyclerView.LayoutParams mResetLayoutParams;

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
                .inflate(R.layout.fragment_suggest_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).name);

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
                    //System.out.println("TEST2");
                    //holder.updateVisibility();
                }
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {

    }

    public void onFilterUpdate()
    {
        System.out.println("TEST");
        //mRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
    }

    @Override
    public int getItemCount()
    {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public DataListItem mItem;

        public ViewHolder(View view)
        {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.name);
            //updateVisibility();
        }

        public void updateVisibility()
        {
            if (mItem != null)
            {
                int visible = (mItem.state == DataListItem.DataItemState.SUGGESTED ? View.VISIBLE : View.GONE);
                mView.setVisibility(visible);
                mView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
