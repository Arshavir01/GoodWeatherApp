package com.example.user.goodweatherapp;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        final DatePicker datePicker = (DatePicker)findViewById(R.id.datePickerId);

        Toast.makeText(getApplicationContext(),
                datePicker.getDayOfMonth()+"/"+
                        datePicker.getMonth()+"/"+
                        datePicker.getYear(), Toast.LENGTH_LONG).show();
    }
}
