package com.ingwersen.kyle.cs125_project;

import java.time.Duration;

/**
 * Created by kyle on 3/9/2018.
 */

public class Util
{
    public static String formatTime(Duration difference)
    {
        if (difference.toMillis() < 60000)
        {
            long seconds = difference.toMillis() / 1000;
            return String.valueOf(seconds) + " Second" + (seconds == 1 ? "" : "s");
        }
        if (difference.toMinutes() < 60)
        {
            long minutes = difference.toMinutes();
            return String.valueOf(minutes) + " Minute" + (minutes == 1 ? "" : "s");
        }
        else if (difference.toHours() < 24)
        {
            long hours = difference.toHours();
            return String.valueOf(hours) + " Hours" + (hours == 1 ? "" : "s");
        }
        else
        {
            long days = difference.toDays();
            return String.valueOf(days) + " Days" + (days == 1 ? "" : "s");
        }
    }
}
