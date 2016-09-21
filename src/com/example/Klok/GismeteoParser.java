package com.example.Klok;

import android.os.StrictMode;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * Created by YanDoroshenko github.com/YanDoroshenko
 */
class GismeteoParser implements WeatherParser {
    @Override
    public Weather getWeather() throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String main = "https://gismeteo.com";

        Connection connection = Jsoup.connect(main).timeout(5000);
        Document document = connection.get();
        Element temp = document.getElementsByClass("temp").get(0);
        String current_temperature = temp.getElementsByClass("c").text();
        boolean belowZero = current_temperature.charAt(0) == '-';
        current_temperature = current_temperature.substring(1, current_temperature.length() - 2);
        String measurements = temp.getElementsByClass("meas").get(0).text();
        String conditionStr = document.getElementsByClass("cloudness").first().getElementsByTag("td").text();


        connection = Jsoup.connect(document.getElementsByClass("fcast").select("a").attr("abs:href"));
        document = connection.get();
        temp = document.getElementsByClass("swtab").first().getElementsByClass("temp").get(0);
        String high_temperature = " " + temp.getElementsByClass("c").get(1).text() + measurements.substring(0, 1);
        String low_temperature = " " + temp.getElementsByClass("c").get(0).text() + measurements.substring(0, 1);

        WeatherCondition condition = WeatherCondition.condition(conditionStr);

        return new Weather(condition,belowZero,current_temperature, high_temperature, low_temperature, measurements);
    }
}
