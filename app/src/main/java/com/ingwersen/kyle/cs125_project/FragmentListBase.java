package com.ingwersen.kyle.cs125_project;

import android.os.Bundle;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by kyle on 3/8/2018.
 */

public class FragmentListBase<T extends Parcelable> extends FragmentBase
{
    private static final String ARG_LIST_OBJ = "list-obj";
    private ArrayList<T> mList = new ArrayList<>();

    public FragmentListBase()
    {

    }

    public static <T extends Parcelable> FragmentListBase<T> newInstance(ArrayList<T> list)
    {
        FragmentListBase<T> fragment = new FragmentListBase<>();
        fragment.setArguments(fragment.args());
        return fragment;
    }

    protected Bundle args(ArrayList<T> list)
    {
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_LIST_OBJ, list);
        return super.args(args);
    }

    protected Bundle args(Bundle args, ArrayList<T> list)
    {
        args.putParcelableArrayList(ARG_LIST_OBJ, list);
        return super.args(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            // TODO: Pass by reference?
            mList = getArguments().getParcelableArrayList(ARG_LIST_OBJ);
        }
    }
}
