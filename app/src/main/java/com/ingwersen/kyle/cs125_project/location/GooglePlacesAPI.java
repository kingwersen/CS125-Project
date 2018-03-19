package com.ingwersen.kyle.cs125_project.location;

import android.location.Location;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kyle on 3/19/2018.
 */

public class GooglePlacesAPI
{
    // Key: AIzaSyC2Yw0cC0JPxLNVOS3lIxg-0yzkxb3Jyfg
    private static final String KEY = "AIzaSyC2Yw0cC0JPxLNVOS3lIxg-0yzkxb3Jyfg";
    private static final String BASE = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

    public static String getPlaces(Location location)
    {
        // https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=37.421998,-122.084000&radius=100&key=AIzaSyC2Yw0cC0JPxLNVOS3lIxg-0yzkxb3Jyfg
        String url = String.format("%slocation=%f,%f&radius=100&key=%s", BASE, location.getLatitude(), location.getLongitude(), KEY);
        return getWebContent(url);
    }

    private static String getWebContent(String url)
    {
        // https://www.androidauthority.com/use-remote-web-api-within-android-app-617869/
        try
        {
            URL url2 = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) url2.openConnection();
            try
            {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally
            {
                urlConnection.disconnect();
            }

        }
        catch (Exception e)
        {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    private static ArrayList<String> parseForPlaces(String json)
    {
        json = json.replaceAll("\\s", "");
        ArrayList<String> results = new ArrayList<String>();
        boolean hasMore = true;
        int i1 = 0;
        while (hasMore)
        {
            // "types" : [ "locality", "political" ],
            i1 = json.indexOf("\"types\"", i1);
            if (i1 >= 0)
            {
                int i2 = json.indexOf(":", i1);
                int i3 = json.indexOf("[", i1);
                int i4 = json.indexOf(",", i1);
                if (i3 < i4)
                {
                    i3 = json.indexOf("]", i1);
                    i4 = json.indexOf(",", i4);
                }

                String word = json.substring(i2+1, i4).replaceAll("[\\[\\]]", "");
                results.addAll(Arrays.asList(word.split(",")));
            }
            else
            {
                hasMore = false;
            }
        }
        return results;
    }

    //Ex:
    /*
    {
   "html_attributions" : [],
   "results" : [
      {
         "geometry" : {
            "location" : {
               "lat" : 37.3860517,
               "lng" : -122.0838511
            },
            "viewport" : {
               "northeast" : {
                  "lat" : 37.469887,
                  "lng" : -122.0446721
               },
               "southwest" : {
                  "lat" : 37.3567599,
                  "lng" : -122.1178619
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/geocode-71.png",
         "id" : "bb51f066ff3fd0b033db94b4e6172da84b8ae111",
         "name" : "Mountain View",
         "photos" : [
            {
               "height" : 1086,
               "html_attributions" : [
                  "\u003ca href=\"https://maps.google.com/maps/contrib/104290863012178742526/photos\"\u003eAyon Saha\u003c/a\u003e"
               ],
               "photo_reference" : "CmRaAAAA-DyVchcD4rHnw4QD7iFKzdDQkyvHRH3YQbIo0dSVXslpV9W4M2EarY5yMLYfPQt55YXWZH1D4Zgw8pGcJ-3OTOPCBln3_PXa5XnSpc48-7t5RxHbTmrsP6xvOpPmqsboEhCksxgU7eMTe7bpB-5dfT00GhTC7ieoyvdTk3qSSWpl5Ioq0NcDDA",
               "width" : 1630
            }
         ],
         "place_id" : "ChIJiQHsW0m3j4ARm69rRkrUF3w",
         "reference" : "CmRbAAAAwV8FZyvi2hGdUnU35mLdfh4OvDdzqsMItrklwAMjHQoOfuq0EztCBckwczz7wsDnBxN98UuN5aV1oWdhMvqfrkP0peXbmyeCdLPXUFF5FNk5v2R7H9bWuHjslPa4u2_2EhBLCDhT0fVzcLNdrx0fPdMtGhSO-EYWwdF9pBiGeXwOu7Mj9QPJGw",
         "scope" : "GOOGLE",
         "types" : [ "locality", "political" ],
         "vicinity" : "Mountain View"
      },
     */
}
