package com.example.Klok;

/**
 * Enum. containing all the supported weather conditions
 *
 * @author Yan Doroshenko
 */
enum WeatherCondition {

    Fair("Fair", R.drawable.fair),
    FairSmoke("Fair, smoke", R.drawable.fairsmoke),
    Brume("Brume", R.drawable.brume),
    PartlyCloudy("Partly cloudy", R.drawable.partlycloudy),
    Cloudy("Cloudy", R.drawable.cloudy),
    MainlyCloudy("Mainly cloudy", R.drawable.mainlycloudy),
    MainlyCloudyBrume("Mainly cloudy, brume", R.drawable.mainlycloudybrume),
    MainlyCloudyRain("Mainly cloudy, rain", R.drawable.mainlycloudyrain),
    MainlyCloudyRainStorm("Mainly cloudy, rain, storm", R.drawable.mainlycloudyrainstorm),
    Rain("Rain", R.drawable.rain),
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
