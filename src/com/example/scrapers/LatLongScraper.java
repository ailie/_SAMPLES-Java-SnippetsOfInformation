package com.example.scrapers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LatLongScraper {

    public static void main(String[] args) {

        String userInput = "abc (-90, +147.45) def\nghi (32.321,-64.123) jkl ";

        for (double[] latAndLong : scrapeLatsAndLongsFrom(userInput))
            System.out.format("Lat: %f Lng: %f\n", latAndLong[0], latAndLong[1]);
    }

    /**
     * This extracts Lat-Long pairs from the given string.
     *
     * @return A list of Lat-Long pairs.
     */
    private static List<double[]> scrapeLatsAndLongsFrom(String stringContainingLatlong) {

        final String regex_num = "([-+]?[1-9][0-9]*(?:\\.[0-9]+)?)";

        Matcher m = Pattern.compile(".*\\(" + regex_num + ",\\s*" + regex_num + "\\).*")
                           .matcher(stringContainingLatlong);

        List<double[]> result = new ArrayList<>();

        while (m.find()) {
            Double lat = Double.valueOf(m.group(1)), lng = Double.valueOf(m.group(2));
            if (isInRange(lat) && isInRange(lng))
                result.add(new double[] { lat, lng });
        }

        return result;
    }

    private static boolean isInRange(Double num) {
        num = Math.abs(num);
        return (1 <= num && num <= 180) ? true : false;
    }
}
