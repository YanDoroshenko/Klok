package com.example.Klok;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Button button = (Button) findViewById(R.id.weather_switch);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WidgetProvider.setHasWeather(!WidgetProvider.isHasWeather());
                if (WidgetProvider.isHasWeather())
                    ((Button) findViewById(R.id.weather_switch)).setText(R.string.weather_off);
                else
                    ((Button) findViewById(R.id.weather_switch)).setText(R.string.weather_on);
                Context context = getApplicationContext();
                CharSequence text = WidgetProvider.isHasWeather() ? "Weather will appear after widget update" : "Weather will disappear after widget update";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }
}
