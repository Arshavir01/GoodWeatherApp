package com.example.user.goodweatherapp;

import android.content.Context;
import android.os.AsyncTask;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 03.05.2017.
 */

public class WeatherService extends AsyncTask<String, String, String > {
    static String finalSunrise;
    static String finalSunset;
    static double humidity;
    static double pressure;
    static double tempMin;
    static double tempMax;
    static double speed;

    Context context;

    public WeatherService(Context context){
        this.context = context;
    }


    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection)url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            String line = "";
            StringBuilder builder = new StringBuilder();

            while ((line = reader.readLine()) != null){
                builder.append(line);
            }

            String finalResult = builder.append(line).toString();
            return finalResult;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        MainActivity.dialog.dismiss();

        try {
            JSONObject parentObject = new JSONObject(result);

            //sys sunrise
            JSONObject sysObject = parentObject.getJSONObject("sys");

            double sunrise = Double.parseDouble(sysObject.getString("sunrise"));
            double timestempSunrise = sunrise * 1000L;
            Date date = new Date((long)timestempSunrise);
            finalSunrise = new SimpleDateFormat("hh:mm").format(date);

            double sunset = Double.parseDouble(sysObject.getString("sunset"));
            double timestempSunset = sunset * 1000L;
            Date date1 = new Date((long)timestempSunset);
            finalSunset = new SimpleDateFormat("hh:mm").format(date1);
            // end sys


            //weather
            JSONArray jsonArray = parentObject.getJSONArray("weather");
            JSONObject weatherObj = jsonArray.getJSONObject(0);
            String desc = weatherObj.getString("description"); //Clouds
            String icon = weatherObj.getString("icon");

            //Picasso Library
            Picasso.with(context).load("http://openweathermap.org/img/w/"+icon+".png").into(MainActivity.iconImage);

            //main
            JSONObject jsonObject = new JSONObject(parentObject.getString("main"));//main object

            double temperature = Double.parseDouble(jsonObject.getString("temp"));
            double temperatureFar = temperature *9/5+32; //formula Farenhite

            tempMin = Double.parseDouble(jsonObject.getString("temp_min"));
            tempMax = Double.parseDouble(jsonObject.getString("temp_max"));


            humidity = Double.parseDouble(jsonObject.getString("humidity"));
            pressure = Double.parseDouble(jsonObject.getString("pressure"));
            //end main

            //wind
            JSONObject windObject = new JSONObject(parentObject.getString("wind"));
            speed = Double.parseDouble(windObject.getString("speed"));
            //end wind

            String countryName = parentObject.getString("name"); //String country name from JSON


            MainActivity.countryTv.setText(countryName);
            MainActivity.temperatureTv.setText(String.valueOf(new DecimalFormat("##.#").format(temperature))+"°C"+ " | "
                    + new DecimalFormat("##.#").format(temperatureFar)+"°F");
            MainActivity.description.setText(desc);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}