package me.ponktacology.clashmc.core.util;

import lombok.experimental.UtilityClass;
import me.ponktacology.clashmc.api.util.MathUtil;
import org.apache.commons.lang.StringEscapeUtils;



@UtilityClass
public class StyleUtil {


  public static String state(boolean bool) {
    return bool ? "&awłączono" : "&cwyłączono";
  }


  public static String formatPercentage(double percentage) {
    return MathUtil.roundOff(percentage * 100, 1) + "%";
  }

  public static String colorPing(int ping) {
    String coloredPing;
    if (ping <= 40) {
      coloredPing = "&a" + ping;
    } else if (ping <= 70) {
      coloredPing = "&6" + ping;
    } else if (ping <= 100) {
      coloredPing = "&e" + ping;
    } else {
      coloredPing = "&c" + ping;
    }

    return coloredPing;
  }

  
  public static String colorHealth(double health) {
    String coloredHealth;
    if (health > 15) {
      coloredHealth = "&a" + convertHealth(health);
    } else if (health > 10) {
      coloredHealth = "&6" + convertHealth(health);
    } else if (health > 5) {
      coloredHealth = "&e" + convertHealth(health);
    } else {
      coloredHealth = "&c" + convertHealth(health);
    }

    return Text.colored(coloredHealth);
  }

  public static double convertHealth(double health) {
    double dividedHealth = health / 2;

    if (dividedHealth % 1 == 0) {
      return dividedHealth;
    }

    if (dividedHealth % .5 == 0) {
      return dividedHealth;
    }

    if (dividedHealth - ((int) dividedHealth) > .5) {
      return ((int) dividedHealth) + 1;
    } else if (dividedHealth - ((int) dividedHealth) > .25) {
      return ((int) dividedHealth) + .5;
    } else {
      return ((int) dividedHealth);
    }
  }

  public static String getHeartIcon() {
    return StringEscapeUtils.unescapeJava("\u2764");
  }

  
  public static String convertBooleanToText(boolean bool) {
    return Text.colored(bool ? "&aTak" : "&cNie");
  }
}
