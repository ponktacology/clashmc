package me.ponktacology.clashmc.api.util;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class TimeUtil {

  public static int convertTimeToTicks(long time, TimeUnit unit) {
    return (int) (TimeUnit.MILLISECONDS.convert(time, unit) / 50);
  }

  public static String formatTimeMillis(long millis) {
    if (millis == -1) {
      return "nigdy";
    }

    long seconds = millis / 1000L;

    if (seconds <= 0) {
      return "teraz";
    }

    long minutes = seconds / 60;
    seconds = seconds % 60;
    long hours = minutes / 60;
    minutes = minutes % 60;
    long day = hours / 24;
    hours = hours % 24;
    long years = day / 365;
    day = day % 365;

    StringBuilder time = new StringBuilder();

    if (years != 0) {
      time.append(years).append("r");
    }

    if (day != 0) {
      time.append(day).append("d");
    }

    if (hours != 0) {
      time.append(hours).append("h");
    }

    if (minutes != 0) {
      time.append(minutes).append("m");
    }

    if (seconds != 0) {
      time.append(seconds).append("s");
    }

    return time.toString().trim();
  }


  public static String formatTimeSeconds(long seconds) {
    return formatTimeMillis(seconds * 1000);
  }


  public static String formatTimeMillisToClock(long millis) {
    return millis / 1000L <= 0
        ? "0:00"
        : String.format(
            "%01d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millis)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
            TimeUnit.MILLISECONDS.toSeconds(millis)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
  }


  public static String formatTimeMillisToTimeOnlyDate(long millis) {
    return new SimpleDateFormat("HH:mm").format(new Date(millis));
  }


  public static String formatTimeMillisToDate(long millis) {
    if (millis < 0) return "nigdy";

    return new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(new Date(millis));
  }


  public static String formatTimeSecondsToClock(long seconds) {
    return formatTimeMillisToClock(seconds * 1000);
  }

  public static long parseTime( String time) {
    long totalTime = 0L;
    boolean found = false;
    Matcher matcher = Pattern.compile("\\d+\\D+").matcher(time);

    while (matcher.find()) {
      String s = matcher.group();
      long value = Long.parseLong(s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0]);
      String type = s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[1];

      switch (type) {
        case "s":
          {
            totalTime += value;
            found = true;
            continue;
          }
        case "m":
          {
            totalTime += value * 60L;
            found = true;
            continue;
          }
        case "h":
          {
            totalTime += value * 60L * 60L;
            found = true;
            continue;
          }
        case "d":
          {
            totalTime += value * 60L * 60L * 24L;
            found = true;
            continue;
          }
        case "w":
          {
            totalTime += value * 60L * 60L * 24L * 7L;
            found = true;
            continue;
          }
        case "mo":
          {
            totalTime += value * 60L * 60L * 24L * 30L;
            found = true;
            continue;
          }
        case "y":
          {
            totalTime += value * 60L * 60L * 24L * 365L;
            found = true;
          }
      }
    }

    return found ? (totalTime * 1000L) : -1L;
  }
}
