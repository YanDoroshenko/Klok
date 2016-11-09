package com.example.Klok;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.RemoteViews;

import java.io.IOException;
import java.util.Date;

/**
 * Created by YanDoroshenko github.com/YanDoroshenko
 */
public class WeatherWidget extends AppWidgetProvider {

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        DataStorage storage = new DataStorage(context.getApplicationContext());
        storage.resetUpdated();
        storage.writeHasWeather(true);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
        DataStorage storage = new DataStorage(context.getApplicationContext());

        if (storage.readHasWeather()) {
            drawSmallBattery(new BatteryReader(context).getBattery(), remoteViews);
            AlarmReader alarm = new AlarmReader(context);
            drawSmallAlarm(alarm.alarmSet(), alarm.readAlarm(), remoteViews);
            try {
                drawWeather(new GismeteoParser().getWeather(), remoteViews, context);
                storage.writeUpdated();
            } catch (IOException e) {
                if (storage.readUpdated() == -1) {
                    drawError(remoteViews);
                } else if (new Date().getTime() - storage.readUpdated() > 7200000) {
                    drawOutdated(remoteViews);
                }
            }
        } else {
            hideWeather(remoteViews);
            drawLargeBattery(new BatteryReader(context).getBattery(), remoteViews);
            AlarmReader alarm = new AlarmReader(context);
            drawLargeAlarm(alarm.alarmSet(), alarm.readAlarm(), remoteViews);
        }

        /*Launch system alarm app on clock click*/
        Intent openClockIntent = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
        openClockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent clockPendingIntent = PendingIntent.getActivity(context, 0, openClockIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.textClock, clockPendingIntent);

        /*Update clock_only_widget on everything else click*/
        Intent intent = new Intent(context, WeatherWidget.class);
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

        /*Redrawing and updating clock_only_widget*/
        appWidgetManager.updateAppWidget(appWidgetIds[0], remoteViews);
    }

    private void drawLargeBattery(int level, RemoteViews remoteViews) {
        if (level < 5)
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery0);
        else if (level < 10)
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery5);
        else if (level < 20)
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery10);
        else if (level < 30)
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery20);
        else if (level < 40)
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery30);
        else if (level < 50)
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery40);
        else if (level < 60)
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery50);
        else if (level < 70)
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery60);
        else if (level < 80)
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery70);
        else if (level < 90)
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery80);
        else if (level < 95)
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery90);
        else
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery100);
        remoteViews.setTextViewText(R.id.battery_text, " " + level + "%  ");
        remoteViews.setTextViewTextSize(R.id.battery_text, 1, 15.0f);
    }

    private void drawSmallBattery(int level, RemoteViews remoteViews) {
        if (level < 5)
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery0_small);
        else if (level < 10)
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery5_small);
        else if (level < 20)
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery10_small);
        else if (level < 30)
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery20_small);
        else if (level < 40)
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery30_small);
        else if (level < 50)
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery40_small);
        else if (level < 60)
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery50_small);
        else if (level < 70)
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery60_small);
        else if (level < 80)
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery70_small);
        else if (level < 90)
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery80_small);
        else if (level < 95)
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery90_small);
        else
            remoteViews.setImageViewResource(R.id.battery_icon, R.drawable.battery100_small);
        remoteViews.setTextViewText(R.id.battery_text, " " + level + "%  ");
        remoteViews.setTextViewTextSize(R.id.battery_text, 1, 12.0f);
    }

    private void drawSmallAlarm(boolean hasAlarm, String nextAlarm, RemoteViews remoteViews) {
        if (hasAlarm) {
            remoteViews.setImageViewResource(R.id.alarm_icon, R.drawable.alarm_small);
            remoteViews.setTextViewText(R.id.alarm_text, nextAlarm);
            remoteViews.setTextViewTextSize(R.id.alarm_text, 1, 12.0f);
        } else {
            remoteViews.setImageViewResource(R.id.alarm_icon, 0);
            remoteViews.setTextViewText(R.id.alarm_text, "");
        }
    }

    private void drawLargeAlarm(boolean hasAlarm, String nextAlarm, RemoteViews remoteViews) {
        if (hasAlarm) {
            remoteViews.setImageViewResource(R.id.alarm_icon, R.drawable.alarm);
            remoteViews.setTextViewText(R.id.alarm_text, nextAlarm);
            remoteViews.setTextViewTextSize(R.id.alarm_text, 1, 15.0f);
        } else {
            remoteViews.setImageViewResource(R.id.alarm_icon, 0);
            remoteViews.setTextViewText(R.id.alarm_text, "");
        }
    }

    private void drawWeather(Weather weather, RemoteViews remoteViews, Context context) {
        showWeather(remoteViews);
        remoteViews.setViewVisibility(R.id.not_updated, 0);
        remoteViews.setTextColor(R.id.weather_text, Color.LTGRAY);
        remoteViews.setTextViewTextSize(R.id.weather_text, 1, 25.0f);
        remoteViews.setTextColor(R.id.weather_text, Color.LTGRAY);
        if (weather.getTemperature().equals("")) {
            remoteViews.setTextViewText(R.id.sign, " ");
            remoteViews.setTextViewText(R.id.weather_text, "0");
        } else {
            remoteViews.setTextViewText(R.id.sign, " " + (weather.isBelowZero() ? String.valueOf((char)0x2013) : "+"));
        }
        remoteViews.setTextViewText(R.id.weather_text, weather.getTemperature());
        remoteViews.setTextViewText(R.id.measurements, weather.getMeasurements());
        remoteViews.setTextViewText(R.id.high_temperature, weather.getHi());
        remoteViews.setTextViewText(R.id.low_temperature, weather.getLo());
        remoteViews.setViewVisibility(R.id.not_updated, View.GONE);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), weather.getCondition().getIconId());
        remoteViews.setImageViewBitmap(R.id.weather_icon, icon);
    }

    private void showWeather(RemoteViews remoteViews) {
        remoteViews.setViewVisibility(R.id.not_updated, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.weather_text, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.weather_icon, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.high_temperature, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.low_temperature, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.measurements, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.sign, View.VISIBLE);

    }

    private void hideWeather(RemoteViews remoteViews) {
        remoteViews.setViewVisibility(R.id.not_updated, View.GONE);
        remoteViews.setViewVisibility(R.id.weather_text, View.GONE);
        remoteViews.setViewVisibility(R.id.weather_icon, View.GONE);
        remoteViews.setViewVisibility(R.id.high_temperature, View.GONE);
        remoteViews.setViewVisibility(R.id.low_temperature, View.GONE);
        remoteViews.setViewVisibility(R.id.measurements, View.GONE);
        remoteViews.setViewVisibility(R.id.sign, View.GONE);

    }

    private void drawError(RemoteViews remoteViews) {
        remoteViews.setTextColor(R.id.weather_text, Color.RED);
        remoteViews.setTextViewText(R.id.weather_text, "Error getting weather!");
        remoteViews.setViewVisibility(R.id.weather_icon, 0);
        remoteViews.setViewVisibility(R.id.weather_text, 0);
        remoteViews.setViewVisibility(R.id.high_temperature, 0);
        remoteViews.setViewVisibility(R.id.low_temperature, 0);
        remoteViews.setViewVisibility(R.id.sign, 0);
        remoteViews.setViewVisibility(R.id.measurements, 0);
    }

    private void drawOutdated(RemoteViews remoteViews) {
        remoteViews.setViewVisibility(R.id.not_updated, View.VISIBLE);
    }
}
