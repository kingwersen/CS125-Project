package com.ingwersen.kyle.cs125_project;

/**
 * Created by kyle on 2/28/2018.
 */

public enum Navigation
{
    SUGGEST(R.string.title_suggest, R.layout.fragment_suggest),
    CART(R.string.title_cart, R.layout.fragment_cart),
    HISTORY(R.string.title_history, R.layout.fragment_history);

    private int mTitleResId;
    private int mLayoutResId;

    Navigation(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}