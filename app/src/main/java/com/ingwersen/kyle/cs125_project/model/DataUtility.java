package com.ingwersen.kyle.cs125_project.model;
import com.ingwersen.kyle.cs125_project.Util;
import com.ingwersen.kyle.cs125_project.model.DataModel.DataListItem;

import java.time.Duration;
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
            // TODO: Calculate and Modify item.utility
            // Utility from distance from expected mean
            long dist = Duration.between(Util.currentTime(), item.timeLast).getSeconds();
            float fromHistory = item.count > 1 ? pretendGaussianDensity(dist, item.timeMean, item.timeStdDev) : 0;

            item.utility = WEIGHTS[0] * fromHistory;
        }
    }

    public static float pretendGaussianDensity(float x, float mean, float sd)
    {
        float diff = Math.abs(x - mean);
        return Math.max(1 - diff / sd / 2f, 0f); // Careful: sd == 0 when n < 2.
    }
}
