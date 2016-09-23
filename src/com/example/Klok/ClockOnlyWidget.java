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
public class ClockOnlyWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.clock_only_widget);

        /*Launch system alarm app on clock click*/
        Intent openClockIntent = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
        openClockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent clockPendingIntent = PendingIntent.getActivity(context, 0, openClockIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.textClock, clockPendingIntent);

        /*Redrawing and updating clock_only_widget*/
        appWidgetManager.updateAppWidget(appWidgetIds[0], remoteViews);
    }
}
