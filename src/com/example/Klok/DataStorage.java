package com.example.Klok;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

/**
 * Created by YanDoroshenko github.com/YanDoroshenko
 */
class DataStorage {

    private final Context context;

    DataStorage(Context context) {
        this.context = context;
    }

    boolean readHasWeather() {
        SharedPreferences preferences = context.getSharedPreferences("storage", Context.MODE_PRIVATE);
        return preferences.getBoolean("hasWeather", true);
    }

    private void writeHasWeather(boolean hasWeather) {
        SharedPreferences preferences = context.getSharedPreferences("storage", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("hasWeather", hasWeather).apply();
    }

    void alterWeather() {
        writeHasWeather(!readHasWeather());
    }

    long readUpdated() {
        SharedPreferences preferences = context.getSharedPreferences("storage", Context.MODE_PRIVATE);
        return preferences.getLong("updatedAt", -1);
    }

    void writeUpdated() {
        SharedPreferences preferences = context.getSharedPreferences("storage", Context.MODE_PRIVATE);
        preferences.edit().putLong("updatedAt", new Date().getTime()).apply();
    }

    void resetUpdated() {
        SharedPreferences preferences = context.getSharedPreferences("storage", Context.MODE_PRIVATE);
        preferences.edit().putLong("updatedAt", -1).apply();
    }
}
