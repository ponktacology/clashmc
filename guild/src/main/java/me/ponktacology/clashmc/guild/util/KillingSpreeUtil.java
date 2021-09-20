package me.ponktacology.clashmc.guild.util;



public class KillingSpreeUtil {

  public static String convert(int spree, RankUtil.Type type) {
    switch (spree) {
      case 2:
        {
          return "DOUBLEKILL!";
        }
      case 3:
        {
          return "TRIPLEKILL!";
        }
      case 4:
        {
          return "QUADRAKILL!";
        }
      case 5:
        {
          return "PENTAKILL!";
        }
      case 6:
        {
          return "HEXAKILL!";
        }
      default:
        {
          return type == RankUtil.Type.WHILE_EATING ? "Lizu!" : "Zabiłeś!";
        }
    }
  }
}
