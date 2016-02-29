package com.example.Klok;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.widget.RemoteViews;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Date;


/**
 * Created by yan on 04.02.16.
 */
public class WidgetProvider extends AppWidgetProvider {
    private static boolean hasWeather = true;

    public static boolean isHasWeather() {
        return hasWeather;
    }

    public static void setHasWeather(boolean hasWeather) {
        WidgetProvider.hasWeather = hasWeather;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        /*Battery readers*/
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int batteryPct = (int) (level / (float) scale * 100);

        /*Remote views change contents of layout*/
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        /*Getting alarm*/
        String nextAlarm = Settings.System.getString(context.getContentResolver(),
                Settings.System.NEXT_ALARM_FORMATTED);
        boolean hasAlarm = nextAlarm.length() > 1;
        Settings.System.getString(context.getContentResolver(),
                Settings.System.NEXT_ALARM_FORMATTED);

        if (!hasWeather) {
            /*Not showing weather*/
            remoteViews.setTextViewText(R.id.weather_text, "");
            remoteViews.setTextViewText(R.id.high_temperature, "");
            remoteViews.setTextViewText(R.id.low_temperature, "");
            remoteViews.setImageViewResource(R.id.weather_icon, 0);
            remoteViews.setTextViewText(R.id.not_updated, "");
            /*Drawing battery*/
            if (batteryPct < 20)
                remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery00);
            else if (batteryPct >= 20 && batteryPct < 40)
                remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery20);
            else if (batteryPct >= 40 && batteryPct < 60)
                remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery40);
            else if (batteryPct >= 60 && batteryPct < 80)
                remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery60);
            else if (batteryPct >= 80 && batteryPct < 95)
                remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery80);
            else
                remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery100);
            remoteViews.setTextViewText(R.id.battery_text, batteryPct + "%  ");
            remoteViews.setTextViewTextSize(R.id.battery_text, 1, 15.0f);
            if (hasAlarm) {
                remoteViews.setImageViewResource(R.id.alarm_icon, R.drawable.alarm);
                remoteViews.setTextViewText(R.id.alarm_text, nextAlarm);
                remoteViews.setTextViewTextSize(R.id.alarm_text, 1, 15.0f);
            } else {
                remoteViews.setImageViewResource(R.id.alarm_icon, 0);
                remoteViews.setTextViewText(R.id.alarm_text, "");
            }
        } else {

            /*Setting persistent variable to store last update time*/
            SharedPreferences updatedTimeStorage = PreferenceManager.getDefaultSharedPreferences(context);

            /*Getting and drawing weather*/
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Connection connection = Jsoup.connect("https://weather.yahoo.com").timeout(5000);
                Document document = connection.get();
                String current_temperature = " " + document.getElementsByClass("num").get(1).text() + "°";
                String high_temperature = " " + document.getElementsByClass("hi").get(1).text();
                String low_temperature = " " + document.getElementsByClass("lo").get(1).text();
                String condition = document.getElementsByClass("cond").first().text();
                remoteViews.setTextViewTextSize(R.id.weather_text, 1, 25.0f);
                remoteViews.setTextColor(R.id.weather_text, Color.LTGRAY);
                remoteViews.setTextViewText(R.id.weather_text, current_temperature);
                remoteViews.setTextViewText(R.id.high_temperature, high_temperature);
                remoteViews.setTextViewText(R.id.low_temperature, low_temperature);
                remoteViews.setTextViewText(R.id.not_updated, "");
                updatedTimeStorage.edit().putLong("updatedAt", new Date().getTime()).commit();
                switch (condition) {
                    case "Mostly Clear":
                        remoteViews.setImageViewResource(R.id.weather_icon, R.drawable.mostly_clear);
                        break;
                    case "Showers":
                        remoteViews.setImageViewResource(R.id.weather_icon, R.drawable.showers);
                        break;
                    case "Mostly Cloudy":
                        remoteViews.setImageViewResource(R.id.weather_icon, R.drawable.mostly_cloudy);
                        break;
                    case "Partly Cloudy":
                        remoteViews.setImageViewResource(R.id.weather_icon, R.drawable.partly_cloudy);
                        break;
                    case "Light Rain":
                        remoteViews.setImageViewResource(R.id.weather_icon, R.drawable.light_rain);
                        break;
                    case "Cloudy":
                        remoteViews.setImageViewResource(R.id.weather_icon, R.drawable.cloudy);
                        break;
                    case "Mostly Sunny":
                        remoteViews.setImageViewResource(R.id.weather_icon, R.drawable.mostly_sunny);
                        break;
                    case "Snow":
                        remoteViews.setImageViewResource(R.id.weather_icon, R.drawable.snow);
                        break;
                }
            } catch (Exception e) {
                long updatedAt = updatedTimeStorage.getLong("updatedAt", 0);
                if (updatedAt == 0) {
                    remoteViews.setTextViewTextSize(R.id.weather_text, 1, 12.0f);
                    remoteViews.setTextColor(R.id.weather_text, Color.RED);
                    remoteViews.setTextViewText(R.id.weather_text, "Can't get weather!");
                    remoteViews.setTextViewText(R.id.not_updated, "");
                } else if (new Date().getTime() - updatedAt > 7200000) {
                    remoteViews.setTextColor(R.id.not_updated, Color.RED);
                    remoteViews.setTextViewText(R.id.not_updated, "!");
                }
            }

            /*Drawing battery*/
            if (batteryPct < 20)
                remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery00_small);
            else if (batteryPct >= 20 && batteryPct < 40)
                remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery20_small);
            else if (batteryPct >= 40 && batteryPct < 60)
                remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery40_small);
            else if (batteryPct >= 60 && batteryPct < 80)
                remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery60_small);
            else if (batteryPct >= 80 && batteryPct < 95)
                remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery80_small);
            else
                remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery100_small);
            remoteViews.setTextViewText(R.id.battery_text, batteryPct + "%  ");
            remoteViews.setTextViewTextSize(R.id.battery_text, 1, 12.0f);
            if (hasAlarm) {
                remoteViews.setImageViewResource(R.id.alarm_icon, R.drawable.alarm_small);
                remoteViews.setTextViewText(R.id.alarm_text, nextAlarm);
                remoteViews.setTextViewTextSize(R.id.alarm_text, 1, 12.0f);
            } else {
                remoteViews.setImageViewResource(R.id.alarm_icon, 0);
                remoteViews.setTextViewText(R.id.alarm_text, "");
            }
        }

        /*Launch system alarm app on clock click*/
        Intent openClockIntent = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
        openClockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent clockPendingIntent = PendingIntent.getActivity(context, 0, openClockIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.textClock, clockPendingIntent);


        /*Update widget on everything else click*/
        Intent intent = new Intent(context, WidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.alarm_icon, updatePendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.alarm_text, updatePendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.battery_icon, updatePendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.battery_text, updatePendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.weather_icon, updatePendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.weather_text, updatePendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.low_temperature, updatePendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.high_temperature, updatePendingIntent);

        /*Redrawing and updating widget*/
        appWidgetManager.updateAppWidget(appWidgetIds[0], remoteViews);

    }
}
