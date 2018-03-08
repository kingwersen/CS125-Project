package com.ingwersen.kyle.cs125_project;

import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by kyle on 3/8/2018.
 */

public class FragmentListBase<T> extends FragmentBase
{
    private static final String ARG_LIST_OBJ = "list-obj";
    private ArrayList<T> mList = new ArrayList<>();

    public FragmentListBase()
    {

    }

    public static <T> FragmentListBase<T> newInstance(ArrayList<T> list)
    {
        FragmentListBase<T> fragment = new FragmentListBase<>();
        fragment.setArguments(fragment.args());
        return fragment;
    }

    @Override
    protected Bundle args()
    {
        Bundle args = new Bundle();
        return super.args(args);
    }

    @Override
    protected Bundle args(Bundle args)
    {
        return args;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            mList = getArguments().getParcelableArrayList(ARG_LIST_OBJ);
        }
    }
}
