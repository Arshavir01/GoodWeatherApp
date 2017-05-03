package com.example.user.goodweatherapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    double curLat;
    double curLng;



    static ProgressDialog dialog;
    LocationManager locationManager;
    Location location;
    String PROVIDER ;


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

        //find my current location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        PROVIDER = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        //find my current location from GPS
        location = locationManager.getLastKnownLocation(PROVIDER);
        locationManager.requestLocationUpdates(PROVIDER, 20000, 2, listener);
        showLocation(location);


    }

    // take my current Latitude and Longitude from GPS provider
    public void showLocation(Location location) {
        if (location == null) {
            Toast.makeText(getApplicationContext(), "Location Not Found", Toast.LENGTH_SHORT).show();
            locationManager.requestLocationUpdates(PROVIDER, 0, 0, listener);
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

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
    // end

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(listener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(PROVIDER, 20000, 2, listener);
    }


    LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location == null) {
                Toast.makeText(getApplicationContext(), "Location Not Found", Toast.LENGTH_SHORT).show();
            } else {

                curLat = location.getLatitude();
                curLng = location.getLongitude();

            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getBaseContext(), "GPS is turned on!! ",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderDisabled(String provider) {
            //if GPS is disabele , send user to setting panel for enabling GPS
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);

        }
    };

    //Button
    public void goToMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();

    }
    //Reset Button
    public void resetCurrentWeather(View view) {
        showLocation(location);

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
        showLocation(location);

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
