package com.example.user.goodweatherapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class MainActivity extends AppCompatActivity {

    static TextView countryTv;
    static TextView temperatureTv;
    static TextView description;
    static TextView lattv;
    static TextView lngtv;

    static TextView sunTV; // using in MapActivity
    static TextView sunsetTV; // using in MapActivity
    static ImageView earthImage; // using in MapActivity
    static TextView humidityTV; // using in MapActivity
    static TextView pressureTV; // using in MapActivity
    static TextView tempMinTV; // using in MapActivity
    static TextView tempMaxTV; // using in MapActivity
    static TextView windTV; // using in MapActivity
    static ProgressDialog dialog;

    double curLat;
    double curLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        countryTv = (TextView) findViewById(R.id.countryId);
        temperatureTv = (TextView) findViewById(R.id.tempertaureId);
        description = (TextView) findViewById(R.id.description);
        earthImage = (ImageView)findViewById(R.id.earthImageViewId);

        lattv = (TextView) findViewById(R.id.latId);
        lngtv = (TextView) findViewById(R.id.lngId);
        sunTV = (TextView)findViewById(R.id.sunriseId);
        sunsetTV = (TextView)findViewById(R.id.sunsetId);
        humidityTV = (TextView)findViewById(R.id.humidityId);
        pressureTV = (TextView)findViewById(R.id.pressureId);
        tempMinTV = (TextView)findViewById(R.id.tempMinId);
        tempMaxTV = (TextView)findViewById(R.id.tempMaxId);
        windTV = (TextView)findViewById(R.id.windId);


        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

         showLocation();

    }

    //start crazy things,,,/find my current location
    public void showLocation(){

        //check for my phone location is turned off/on
        if(SmartLocation.with(MainActivity.this).location().state().isAnyProviderAvailable()){

            SmartLocation.with(this).location().start(new OnLocationUpdatedListener() {

                @Override
                public void onLocationUpdated(Location location) {
                    if (location == null) {
                        Toast.makeText(getApplicationContext(), "Location Not Found", Toast.LENGTH_SHORT).show();

                    }else {

                        curLat = location.getLatitude();
                        curLng = location.getLongitude();

                        // Weather service

                        WeatherService weatherService = new WeatherService();
                        weatherService.execute(Common.apiRequest(String.valueOf(curLat), String.valueOf(curLng)));

                        earthImage.setImageResource(R.drawable.earth3);
                        sunTV.setText("");
                        sunsetTV.setText("");
                        humidityTV.setText("");
                        pressureTV.setText("");
                        tempMinTV.setText("");
                        tempMaxTV.setText("");
                        windTV.setText("");


                    }
                }
            });

        } else{

                 Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                 startActivity(intent);
              }

    }
    //end crazy things

    @Override
    protected void onPause() {
        super.onPause();
        // save state is not ready yet
    }

    @Override
    protected void onResume() {
        super.onResume();
        // save state is not ready yet

    }
    //Button
    public void goToMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();

    }
    //Reset Button
    public void resetCurrentWeather(View view) {

        showLocation();

        earthImage.setImageResource(R.drawable.earth3);
        lattv.setText("");
        lngtv.setText("");
        sunTV.setText("");
        sunsetTV.setText("");
        humidityTV.setText("");
        pressureTV.setText("");
        tempMinTV.setText("");
        tempMaxTV.setText("");
        windTV.setText("");

        Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();

        dialog.setMessage("Loading...");
        dialog.show();

    }
    // Show Location Button
    public void latitudeClick(View view) {

        showLocation();

        lattv.setText("Latitude:   " + curLat);
        lngtv.setText("Longitude:  " + curLng);

        earthImage.setImageResource(R.drawable.earth3);
        sunTV.setText("");
        sunsetTV.setText("");
        humidityTV.setText("");
        pressureTV.setText("");
        tempMinTV.setText("");
        tempMaxTV.setText("");
        windTV.setText("");

        Toast.makeText(this, "Current Location", Toast.LENGTH_SHORT).show();

        dialog.setMessage("Loading...");
        dialog.show();

    }
    // Calendar Button
    public void calendarClick(View view) {
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);

    }

    //Details button
    public void detailsClick(View view) {
        earthImage.setImageResource(R.drawable.transparent_earth2);

        sunTV.setText("Sunrise: "+ WeatherService.finalSunrise+" AM");
        sunsetTV.setText("Sunset: "+ WeatherService.finalSunset+" PM");

        humidityTV.setText("Humidity: "+WeatherService.humidity+"%");
        pressureTV.setText("Pressure: "+WeatherService.pressure+"hPa");

        tempMinTV.setText("Temp_Min: "+WeatherService.tempMin+"°C");
        tempMaxTV.setText("Temp_Max: "+WeatherService.tempMax+"°C");

        windTV.setText("Wind: "+WeatherService.speed+"mps");

        Toast.makeText(this, "Details", Toast.LENGTH_SHORT).show();

    }
}
