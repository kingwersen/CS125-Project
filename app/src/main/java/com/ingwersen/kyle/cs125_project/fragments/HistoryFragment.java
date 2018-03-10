package com.ingwersen.kyle.cs125_project.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingwersen.kyle.cs125_project.MainActivity;
import com.ingwersen.kyle.cs125_project.R;
import com.ingwersen.kyle.cs125_project.model.DataModel;
import com.ingwersen.kyle.cs125_project.model.DataModel.DataListItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnHistoryFragmentInteractionListener}
 * interface.
 */
public class HistoryFragment extends Fragment
{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnHistoryFragmentInteractionListener mListener;
    private MainActivity mParent;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HistoryFragment()
    {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static HistoryFragment newInstance(int columnCount, MainActivity parent)
    {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        fragment.mParent = parent;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);
        Context context = view.getContext();

        mRefreshLayout = (SwipeRefreshLayout) view;
        mRefreshLayout.setOnRefreshListener(() -> refreshList());

        for (int i = 0, n = mRefreshLayout.getChildCount(); i < n; ++i)
        {
            View v = mRefreshLayout.getChildAt(i);
            if (v instanceof RecyclerView)
            {
                mRecyclerView = (RecyclerView) v;
                break;
            }
        }
        // TODO: Exit nicely if fails to find RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(new HistoryRecyclerViewAdapter(DataModel.ITEMS, mListener, mParent));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        return view;
    }

    private void refreshList()
    {
        mParent.updateListFilters();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnHistoryFragmentInteractionListener)
        {
            mListener = (OnHistoryFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnSuggestFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnHistoryFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onHistoryFragmentInteraction(DataListItem item);
    }
}
