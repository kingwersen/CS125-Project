package com.ingwersen.kyle.cs125_project.model;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.ingwersen.kyle.cs125_project.R;
import com.ingwersen.kyle.cs125_project.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
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

        init_collab();
    }

    public static void init_collab()
    {
        // TODO: Optimize? Put online?
        try
        {
            List<List<String>> allUsers = Util.parseCsv(mContext.getResources().openRawResource(R.raw.allusers));
            int n_users = (int) Double.parseDouble(allUsers.get(allUsers.size()-1).get(0))+1;
            List<Double> userDiff = new ArrayList<>(n_users);
            for (int i = 0; i < n_users; ++i) userDiff.add(0.0);
            // WARNING: Assuming last item in list is last user ^^^.

            // Get Inverse Difference between users' item means.
            for (List<String> itemData : allUsers)
            {
                int i = (int) Double.parseDouble(itemData.get(0));  // User Number
                int k = (int) Double.parseDouble(itemData.get(1));  // Item Number
                double userMean = ITEMS.get(k).userMean + 1;
                double otherMean = Double.parseDouble(itemData.get(2)) + 1;
                double div = userMean / otherMean;
                double diff = Math.log(div >= 1 ? div : 1 / div) + 1;  // IDF

                // TODO: Get Difference from ALL items. This works for now though.
                userDiff.set(i, userDiff.get(i) + diff);
            }

            // Normalize Differences
            double maxDiff = 0;
            for (double diff : userDiff)
            {
                maxDiff = Math.max(diff, maxDiff);
            }
            for (int i = 0, len = userDiff.size(); i < len; ++i)
            {
                userDiff.set(i, userDiff.get(i) / maxDiff);
            }

            // Construct Un-Normalized Collab Utility
            for (int j = 0, len = ITEMS.size(); j < len; ++j)
            {
                DataListItem item = ITEMS.get(j);
                for (List<String> itemData : allUsers)
                {
                    int k = (int) Double.parseDouble(itemData.get(1));  // Item Number
                    if (j == k)
                    {
                        int i = (int) Double.parseDouble(itemData.get(0));  // User Number

                        // If the user bought the item, suggest it, with a scale based on how
                        // similar the users are.
                        item.totalUtility += userDiff.get(i);
                    }
                }
            }

            // Normalize Utility
            double maxUtility = 0;
            for (DataListItem item : ITEMS)
            {
                maxUtility = Math.max(item.totalUtility, maxUtility);
            }
            for (DataListItem item : ITEMS)
            {
                item.totalUtility /= maxUtility;
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
        public double totalUtility;

        public double util1;
        public double util2;
        public double util3;

        public DataItemState state;

        public DataListItem(String id, String name)
        {
            this.id = id;
            this.name = name;

            this.userCount = 0;
            this.totalCount = 0;
            this.userMean = 0.0;
            this.totalMean = 0.0;
            this.userLast = ZonedDateTime.now();
            this.userUtility = 0.0;
            this.totalUtility = 0.0;

            this.util1 = 0.0;
            this.util2 = 0.0;
            this.util3 = 0.0;

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
