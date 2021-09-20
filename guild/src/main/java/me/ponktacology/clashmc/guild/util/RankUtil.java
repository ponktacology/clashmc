package me.ponktacology.clashmc.guild.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class RankUtil {

  @RequiredArgsConstructor
  @Getter
  public enum Type {
    NORMAL(1.0),
    WHILE_EATING(1.25),
    ON_CUBOID(1.30),
    IN_WAR(1.35);

    private final double multiplier;
  }

  public static int getChangeBetweenTwoScores(int killer, int victim, Type type) {
    if (killer - victim > 300) return 1;

    double actualScore = type.getMultiplier();

    // calculate expected outcome
    double exponent = (double) (victim - killer) / 400;
    double expectedOutcome = (1 / (1 + (Math.pow(10, exponent))));

    // K-factor
    int K = determineK(killer);

    // calculate new rating
    int change = (int) Math.round(K * (actualScore - expectedOutcome));

    return change;
  }

  public static int determineK(int rating) {
    int K;

    if (rating < 1400) {
      K = 50;
    } else if (rating >= 1400 && rating < 2000) {
      K = 42;
    } else if (rating >= 2000 && rating < 2400) {
      K = 36;
    } else if (rating >= 2400 && rating < 28000) {
      K = 28;
    } else {
      K = 20;
    }

    return K;
  }
}
