package me.ponktacology.clashmc.api.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class MathUtil {

    public static double roundOff(double x, int position) {
        return roundOff(x, position, BigDecimal.ROUND_HALF_UP);
    }

    public static double roundOff(float x, int position) {
        return roundOff(x, position);
    }

    public static double roundOff(double x, int scale, int roundingMethod) {
        try {
            return (new BigDecimal
                    (Double.toString(x))
                    .setScale(scale, roundingMethod))
                    .doubleValue();
        } catch (NumberFormatException ex) {
            if (Double.isInfinite(x)) {
                return x;
            } else {
                return Double.NaN;
            }
        }
    }


    public static boolean isBetween(int value, int min, int max) {
        return value >= min && value <= max;
    }

    public static int random(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static double random(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
}
