package com.ingwersen.kyle.cs125_project.model;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.ingwersen.kyle.cs125_project.R;
import com.ingwersen.kyle.cs125_project.Util;
import com.ingwersen.kyle.cs125_project.model.DataModel.DataListItem;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by kyle on 3/10/2018.
 */

public class DataUtility
{
    public static final float[] WEIGHTS = { 1f };

    public static void updateUtility(List<DataListItem> items)
    {
        for (DataListItem item : items)
        {
            // Calculate and Modify item.utility
            // 1. Utility from distance from expected mean
            float dist = (float) Util.timeSince(item.timeLast).getSeconds();
            float fromHistory = item.count > 0 ? pretendGaussianDensity(dist, item.timeMean, item.timeStdDev) : 0;

            // 2. ...

            // 3. ...

            item.utility = fromHistory * WEIGHTS[0]; // + ... + ...
        }
    }

    public static float pretendGaussianDensity(float x, float mean, float sd)
    {
        float diff = Math.abs(x - mean);
        return Math.max(1 - diff / sd / 2f, 0f); // Careful: sd == 0 when n < 2.
    }

    private static Context mContext;
    private static Color[] mGradient;
    public static void setContext(Context context)
    {
        mContext = context;
        mGradient = new Color[]{
                Color.valueOf(ContextCompat.getColor(mContext, R.color.gradientBlue)),
                Color.valueOf(ContextCompat.getColor(mContext, R.color.gradientGreen)),
                Color.valueOf(ContextCompat.getColor(mContext, R.color.gradientYellow)),
                Color.valueOf(ContextCompat.getColor(mContext, R.color.gradientOrange)),
                Color.valueOf(ContextCompat.getColor(mContext, R.color.gradientRed)),
                Color.valueOf(ContextCompat.getColor(mContext, R.color.gradientBlack))
        };
    }

    private static Color gradientBetween(Color a, Color b, float p)
    {
        return Color.valueOf(
                a.red()*(1-p) + b.red()*p,
                a.green()*(1-p) + b.green()*p,
                a.blue()*(1-p) + b.blue()*p
        );
    }

    public static Color itemColor(float timeSince, float mean, float sd)
    {
        if (mGradient == null) {
            throw new InstantiationError("Must set context of DataUtility before use.");
        }

        float percent = timeSince / 4 / sd;
        if (percent < 0.0)
        {
            return mGradient[0];
        }
        else if (percent < 0.2)
        {
            percent = (percent - 0.0f) / (0.2f - 0.0f);
            return gradientBetween(mGradient[0], mGradient[1], percent);
        }
        else if (percent < 0.4)
        {
            percent = (percent - 0.2f) / (0.4f - 0.2f);
            return gradientBetween(mGradient[1], mGradient[2], percent);
        }
        else if (percent < 0.6)
        {
            percent = (percent - 0.4f) / (0.6f - 0.4f);
            return gradientBetween(mGradient[2], mGradient[3], percent);
        }
        else if (percent < 0.8)
        {
            percent = (percent - 0.6f) / (0.8f - 0.6f);
            return gradientBetween(mGradient[3], mGradient[4], percent);
        }
        else if (percent < 1.0)
        {
            percent = (percent - 0.8f) / (1.0f - 0.8f);
            return gradientBetween(mGradient[4], mGradient[5], percent);
        }
        else
        {
            return mGradient[5];
        }
    }
}
