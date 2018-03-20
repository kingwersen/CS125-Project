package com.ingwersen.kyle.cs125_project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kyle on 3/9/2018.
 */

public class Util
{
    public static String formatTime(Duration difference)
    {
        if (difference.toMillis() < 60000)
        {
            long seconds = difference.getSeconds();
            return String.valueOf(seconds) + " Second" + (seconds == 1 ? "" : "s");
        }
        if (difference.toMinutes() < 60)
        {
            long minutes = difference.toMinutes();
            return String.valueOf(minutes) + " Minute" + (minutes == 1 ? "" : "s");
        }
        else if (difference.toHours() < 48)
        {
            long hours = difference.toHours();
            return String.valueOf(hours) + " Hour" + (hours == 1 ? "" : "s");
        }
        else
        {
            long days = difference.toDays();
            return String.valueOf(days) + " Day" + (days == 1 ? "" : "s");
        }
    }

    public static ZonedDateTime currentTime()
    {
        // (In case we want to test future values)
        return ZonedDateTime.now();
    }

    public static Duration timeSince(ZonedDateTime time)
    {
        return Duration.between(time, currentTime());
    }

    public static List<List<String>> parseCsv(InputStream stream)
    {
        // https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
        BufferedReader br = null;
        String line = "";
        String delimiter = "[,\\s]";
        List<List<String>> result = new ArrayList<>();

        try
        {
            br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            while ((line = br.readLine()) != null)
            {
                result.add(Arrays.asList(line.split(delimiter)));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
}
