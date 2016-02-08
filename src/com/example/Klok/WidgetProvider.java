package com.example.Klok;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.StrictMode;
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
    private static Date noUpdateFor;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        /*Battery readers*/
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int batteryPct = (int) (level / (float) scale * 100);

        /*Remote views change contenrs of layout*/
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        /*Getting alarm*/
        String nextAlarm = Settings.System.getString(context.getContentResolver(),
                Settings.System.NEXT_ALARM_FORMATTED);
        boolean hasAlarm = nextAlarm.length() > 1;
        Settings.System.getString(context.getContentResolver(),
                Settings.System.NEXT_ALARM_FORMATTED);

        /*Getting weather*/
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Connection connection = Jsoup.connect("https://weather.yahoo.com");
            Document document = connection.get();
            String current_temperature = " " + document.getElementsByClass("num").get(1).text() + "°";
            String high_temperature = " " + document.getElementsByClass("hi").get(1).text();
            String low_temperature = " " + document.getElementsByClass("lo").get(1).text();
            String condition = document.getElementsByClass("cond").first().text();
            remoteViews.setTextViewText(R.id.weather_text, current_temperature);
            remoteViews.setTextViewText(R.id.high_temperature, high_temperature);
            remoteViews.setTextViewText(R.id.low_temperature, low_temperature);
            switch (condition) {
                case "Mostly Clear":
                    remoteViews.setImageViewResource(R.id.weather_icon, R.drawable.mostly_clear);
                    break;
                case "Showers":
                    remoteViews.setImageViewResource(R.id.weather_icon, R.drawable.showers);
            }
            hasWeather = true;
        } catch (Exception e) {
            noUpdateFor = new Date();
        }
        if (noUpdateFor != null)
            if (new Date().getTime() - noUpdateFor.getTime() > 10800000) {
                remoteViews.setTextColor(R.id.not_updated, Color.RED);
                remoteViews.setTextViewText(R.id.not_updated, "!");
            }

        /*Drawing battery*/
        if (!hasWeather) {
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

        /*Redrawing and updating widget*/
        Intent intent = new Intent(context, WidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.battery_icon, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.battery_text, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetIds[0], remoteViews);
    }
}
