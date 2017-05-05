package com.example.user.goodweatherapp;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity {

    double latMap;
    double lngMap;


    TextView searchCityTv, searchLat, searchLng;
    EditText editText;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        searchCityTv = (TextView)findViewById(R.id.cityId);
        searchLat = (TextView)findViewById(R.id.LatitudeId);
        searchLng = (TextView)findViewById(R.id.LongitudeId);

        editText = (EditText)findViewById(R.id.editTextId);



    }

    // Go button

    public void goBack(View view) throws IOException{

        MainActivity.dialog.show();
        MainActivity.dialog.setMessage("Loading...");
        location = editText.getText().toString();

        MainActivity.sunTV.setText("");
        MainActivity.sunsetTV.setText("");
        MainActivity.humidityTV.setText("");
        MainActivity.pressureTV.setText("");
        MainActivity.tempMinTV.setText("");
        MainActivity.tempMaxTV.setText("");
        MainActivity.windTV.setText("");

        MainActivity.earthImage.setImageResource(R.drawable.earth3);

        Geocoder gc = new Geocoder(this);
        List<Address> list = null;
        try {
            list = gc.getFromLocationName(location, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list == null || list.isEmpty()){
            Toast.makeText(this, "Location is not founded", Toast.LENGTH_SHORT).show();

        } else {

            Address add = list.get(0);
            String locality = add.getLocality();

            latMap = add.getLatitude();
            lngMap = add.getLongitude();

            Toast.makeText(this, locality, Toast.LENGTH_LONG).show();

            String latitudeMap = String.valueOf(latMap);
            String longitudeMap = String.valueOf(lngMap);

            WeatherService weatherService = new WeatherService();
            weatherService.execute(Common.apiRequest(String.valueOf(latitudeMap), String.valueOf(longitudeMap)));

            MainActivity.lattv.setText("");
            MainActivity.lngtv.setText("");

            finish();
        }


    }

    // MapLocation Button
    public void searchLocationClick(View view) {
        location = editText.getText().toString();

        //for deleting previous data
        searchCityTv.setText("");
        searchLat.setText("");
        searchLng.setText("");

        Geocoder gc = new Geocoder(this);
        List<Address> list = null;
        try {
            list = gc.getFromLocationName(location, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list == null || list.isEmpty()){
            Toast.makeText(this, "Location is not founded", Toast.LENGTH_SHORT).show();

        } else {

            Address add = list.get(0);
            String locality1 = add.getLocality();

            latMap = add.getLatitude();
            lngMap = add.getLongitude();


            searchCityTv.setText(locality1);
            searchLat.setText(String.valueOf("Latitude: "+latMap));
            searchLng.setText(String.valueOf("Longitude: "+lngMap));


        }

    }
}
