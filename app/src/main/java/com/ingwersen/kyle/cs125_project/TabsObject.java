package com.ingwersen.kyle.cs125_project;

/**
 * Created by kyle on 2/28/2018.
 */

public enum TabsObject
{
    SUGGESTIONS(R.string.title_suggest, R.layout.view_suggest),
    LIST(R.string.title_list, R.layout.view_cart),
    HISTORY(R.string.title_history, R.layout.view_history);

    private int mTitleResId;
    private int mLayoutResId;

    TabsObject(int titleResId, int layoutResId) {
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