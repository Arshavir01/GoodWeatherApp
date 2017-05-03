package com.example.user.goodweatherapp;


import android.support.annotation.NonNull;



/**
 * Created by user on 3/16/2017.
 */

public class Common {
    public static String API_KEY = "07c2cec609851b780bbb495834301cf7";
    public static String API_LINK = "http://api.openweathermap.org/data/2.5/weather";

    @NonNull
    public static String apiRequest(String lat, String lng){
        StringBuilder sb = new StringBuilder(API_LINK);
        sb.append(String.format("?lat=%s&lon=%s&APPID=%s&units=metric",lat,lng,API_KEY));
        return sb.toString();
    }

}
