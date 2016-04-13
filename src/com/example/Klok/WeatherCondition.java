package com.example.Klok;

/**
 * Enum. containing all the supported weather conditions
 *
 * @author Yan Doroshenko
 */
enum WeatherCondition {

    Fair("Fair", R.drawable.fair),
    MainlyCloudy("Mainly cloudy", R.drawable.mainlycloudy),
    Brume("Brume", R.drawable.brume),
    MainlyCloudyBrume("Mainly cloudy, brume", R.drawable.mainlycloudybrume),
    Cloudy("Cloudy", R.drawable.cloudy),
    MainlyCloudyRainStorm("Mainly cloudy, rain, storm", R.drawable.mainlycloudyrainstorm),
    Unknown("", R.drawable.unknown);


    private String conditionString;
    private int iconId;

    WeatherCondition(String conditionString, int iconId) {
        this.iconId = iconId;
        this.conditionString = conditionString;
    }

    static WeatherCondition condition(String conditionString) {
        for (WeatherCondition condition : values())
            if (condition.conditionString.equals(conditionString))
                return condition;
        return Unknown;
    }

    int getIconId() {
        return iconId;
    }
}
