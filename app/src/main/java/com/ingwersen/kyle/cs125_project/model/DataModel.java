package com.ingwersen.kyle.cs125_project.model;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.ingwersen.kyle.cs125_project.R;
import com.ingwersen.kyle.cs125_project.Util;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample name for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DataModel
{

    public static final List<DataListItem> ITEMS = new ArrayList<DataListItem>();
    public static final List<String> HEADERS = new ArrayList<String>();
    public static Context mContext;

    public static void init(Context context)
    {
        mContext = context;
        try
        {
            HEADERS.clear();
            ITEMS.clear();
            HEADERS.addAll(Util.parseCsv(mContext.getResources().openRawResource(R.raw.headers)).get(0));
            for (int i = 0, len = HEADERS.size(); i < len; ++i)
            {
                ITEMS.add(new DataListItem(
                        String.valueOf(i),  // Position
                        HEADERS.get(i)      // Name
                ));
            }
        }
        catch (Exception e)
        {
            Log.e("DataModel", "Error Initializing:\n" + e);
        }
        init_totals();
        init_user();
    }

    public static void init_totals()
    {
        try
        {
            List<List<String>> totals = Util.parseCsv(mContext.getResources().openRawResource(R.raw.user_totals));
            for (int i = 0, len = totals.size(); i < len; ++i)
            {
                DataListItem item = ITEMS.get(i);
                item.totalMean = Double.parseDouble(totals.get(i).get(0));
                item.totalCount = (int) Double.parseDouble(totals.get(i).get(1));
            }
        }
        catch (Exception e)
        {
            Log.e("DataModel", "Error Initializing Totals:\n" + e);
        }
    }

    public static void init_user()
    {
        // Reset Previous Data
        for (DataListItem item : ITEMS)
        {
            item.userCount = 0;
        }

        try
        {
            List<List<String>> user = Util.parseCsv(mContext.getResources().openRawResource(R.raw.user0));
            for (int i = 0, len = user.size(); i < len; ++i)
            {
                int k = (int) Double.parseDouble(user.get(i).get(0));
                DataListItem item = ITEMS.get(k);
                item.userMean = Double.parseDouble(user.get(i).get(1));
                item.userCount = (int) Double.parseDouble(user.get(i).get(2));
                item.userLast = ZonedDateTime.now().plusSeconds((int) Double.parseDouble(user.get(i).get(3))); // Negative Value
            }
        }
        catch (Exception e)
        {
            Log.e("DataModel", "Error Initializing User:\n" + e);
        }
    }

    public static class DataListItem
    {
        public final String id;
        public final String name;

        public int userCount;
        public int totalCount;
        public double userMean;
        public double totalMean;
        public ZonedDateTime userLast;
        public double userUtility;

        public DataItemState state;

        public DataListItem(String id, String name)
        {
            this.id = id;
            this.name = name;

            this.userCount = 0;
            this.totalCount = 0;
            this.userMean = 0f;
            this.totalMean = 0f;
            this.userLast = ZonedDateTime.now();
            this.userUtility = 0f;

            this.state = DataItemState.SUGGESTED;
        }

        public void incrementUser()
        {
            // Last Time
            ZonedDateTime now = Util.currentTime();
            double timeSince = userSince();
            userLast = now;

            // Count
            ++userCount;

            // Mean
            userMean += (timeSince - userMean) / userCount;

            // Variance
            //timeStdDev = timeMean / 2f;  // [0,mean] = 2 std.dev; [mean,2*mean] = 2 std.dev
            /*
            // https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance
            if (count > 1)
            {
                float var = timeStdDev * timeStdDev;
                var = (count - 2) / (count - 1) * var +
                        (userSince - timeMean) * (userSince - timeMean) / count;
                timeStdDev = (float) Math.sqrt(var);
            }
            */
        }


        public double userSince() {
            return Util.timeSince(userLast).getSeconds();
        }
        public double userStdDev()
        {
            return userMean / 2;
        }
        public double totalStdDev()
        {
            return totalMean / 2;
        }

        @Override
        public String toString()
        {
            return name;
        }

        public enum DataItemState
        {
            SUGGESTED, IN_CART, HIDDEN
        }

        public Color getColor() { return DataUtility.itemColor(userSince(), userMean, userStdDev());}
    }

}
