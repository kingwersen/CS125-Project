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
    public static final double[] WEIGHTS = { 1.0, 1.0, 1.0 };

    public static void updateUtility(List<DataListItem> items)
    {
        for (DataListItem item : items)
        {
            // 1. Utility from distance from expected user mean
            double fromUserHistory = 0;
            if (item.userCount > 1)
            {
                float x = (float) Util.timeSince(item.userLast).getSeconds();
                fromUserHistory = pretendGaussianDensity(x, item.userMean, item.userStdDev());
                item.util1 = fromUserHistory;
            }

            // 2. Utility from distance from expected total mean
            double fromOthersHistory = 0;
            if (item.userCount > 1 && item.totalCount > 1)
            {
                float x = (float) Util.timeSince(item.userLast).getSeconds();
                fromOthersHistory = pretendGaussianDensity(x, item.totalMean, item.totalStdDev());
                item.util2 = fromOthersHistory;
            }

            // 3. Utility from suggesting similar users' items
            double fromSimilarHistory = item.totalUtility;
            item.util3 = fromSimilarHistory;


            // Compute Weighted Sum
            item.userUtility = fromUserHistory * WEIGHTS[0]
                + fromOthersHistory * WEIGHTS[1]
                + fromSimilarHistory * WEIGHTS[2]; // + ... + ...
        }
    }

    public static double pretendGaussianDensity(double x, double mean, double sd)
    {
        double diff = Math.abs(x - mean);
        return Math.max(1 - diff / sd / 2, 0); // Careful: sd == 0 when n < 2.
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

    private static Color gradientBetween(Color a, Color b, double p)
    {
        float p2 = (float) p;
        return Color.valueOf(
                a.red()*(1-p2) + b.red()*p2,
                a.green()*(1-p2) + b.green()*p2,
                a.blue()*(1-p2) + b.blue()*p2
        );
    }

    public static Color itemColor(double timeSince, double mean, double sd)
    {
        if (mGradient == null) {
            throw new InstantiationError("Must set context of DataUtility before use.");
        }

        double percent = timeSince / 4 / sd;
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
