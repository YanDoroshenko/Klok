package com.example.Klok;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by YanDoroshenko github.com/YanDoroshenko
 */
class AlarmReader {

    private final Context context;

    AlarmReader(Context context) {
        this.context = context;
    }

    boolean alarmSet() {
        String nextAlarm = Settings.System.getString(context.getContentResolver(),
                Settings.System.NEXT_ALARM_FORMATTED);
        return nextAlarm.length() > 1;

    }

    String readAlarm() {
        return Settings.System.getString(context.getContentResolver(),
                Settings.System.NEXT_ALARM_FORMATTED);
    }
}
