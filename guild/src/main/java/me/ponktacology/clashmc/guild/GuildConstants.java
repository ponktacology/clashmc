package me.ponktacology.clashmc.guild;

import me.ponktacology.clashmc.core.blazingpack.bar.BarColor;
import me.ponktacology.clashmc.core.blazingpack.bar.BarStyle;
import me.ponktacology.clashmc.core.blazingpack.bar.BossBar;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.sector.SectorConstants;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class GuildConstants {

  public static final long GUILD_CONQUER_DELAY = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS);
  public static final int MAX_CONCURRENT_WARS = 3;
  public static final long GUILD_WAR_DURATION_TIME =
      TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS);
  public static final int MIN_DISTANCE_GUILD_CREATE =
      (GuildConstants.BASE_GUILD_REGION_SIZE * 2) + SectorConstants.MIN_DISTANCE_ALLOW_BUILD;
  public static final long SAME_GUILD_WAR_COOLDOWN =
      TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
  public static final long NO_BUILD_TIME_AFTER_TNT_EXPLODE =
      TimeUnit.MILLISECONDS.convert(2, TimeUnit.MINUTES);
  public static final long WAR_TIME = TimeUnit.MILLISECONDS.convert(2L, TimeUnit.HOURS);
  public static final long WAR_COOLDOWN_TIME = TimeUnit.MILLISECONDS.convert(6L, TimeUnit.HOURS);
  public static final File CENTER_SCHEMATIC =
      new File(
          System.getProperty("user.dir")
              + File.separator
              + "plugins"
              + File.separator
              + "/Guilds/center.schematic");
  public static final int MAX_LIVES = 3;
  public static final long CREATION_PROTECTION_TIME = SAME_GUILD_WAR_COOLDOWN;
  public static final int MAX_HEALTH = 1000;
  public static final int BASE_GUILD_REGION_SIZE = 50;
  public static final int MAX_GUILD_REGION_SIZE = 50;
  public static final long GUILD_RENEW_TIME = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
  public static final long COMBAT_TAG_TIME = TimeUnit.MILLISECONDS.convert(31, TimeUnit.SECONDS);
  public static final long PLACED_BLOCK_CLEAR_TIME =
      TimeUnit.MILLISECONDS.convert(10, TimeUnit.MINUTES);

  public static final BossBar COMBAT_BAR =
      new BossBar(
          UUID.fromString("93301179-9fa9-48bc-8507-c91e105e08d4"),
          BarStyle.SOLID,
          BarColor.RED,
          TextComponent.fromLegacyText(
              Text.colored(
                  "&cJesteś w walce jeszcze przez &f"
                      + TimeUnit.SECONDS.convert(
                          GuildConstants.COMBAT_TAG_TIME, TimeUnit.MILLISECONDS)
                      + "&c sekund")),
          1F);
  public static final BossBar TERRAIN_BAR =
      new BossBar(
          UUID.fromString("2991638d-0ff9-4630-a36e-22fa4d26ef82"),
          BarStyle.SOLID,
          BarColor.RED,
          TextComponent.fromLegacyText(Text.colored("")),
          0F);
  public static final BossBar PROTECTION_BAR =
      new BossBar(
          UUID.fromString("7294c437-2d69-454c-a7fc-5a078bb23c84"),
          BarStyle.SOLID,
          BarColor.GREEN,
          TextComponent.fromLegacyText(Text.colored("&aOchrona startowa: &f5 minut")),
          1F);
  public static final int SPAWN_PROTECTION_SECONDS = 5 * 60;
  public static final long KILLING_SPREE_TIME = TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS);
  public static final int SVIP_HOME_COUNT = 3;
  public static final int VIP_HOME_COUNT = 2;
  public static final long KILL_CACHE_EVICTION_TIME =
      TimeUnit.MILLISECONDS.convert(3, TimeUnit.HOURS);
  public static final long TAB_CACHE_EVICTION_TIME =
      TimeUnit.MILLISECONDS.convert(15, TimeUnit.SECONDS);
  public static final long LAST_ATTACK_EVICTION_TIME =
      TimeUnit.MILLISECONDS.convert(1L, TimeUnit.MINUTES);
  public static final int MIN_TAG_LENGTH = 2;
  public static final int MAX_TAG_LENGTH = 5;
  public static final int MIN_NAME_LENGTH = 4;
  public static final int MAX_NAME_LENGTH = 20;

  public static final String[] AUTO_MESSAGES = {
    "&8[&c&lClash&f&lMC.pl&8] &7Discord&8: &edc.clashmc.pl",
    "&8[&c&lClash&f&lMC.pl&8] &7FanPage&8: &efb.clashmc.pl",
    "&8[&c&lClash&f&lMC.pl&8] &7YouTube&8: &eyt.clashmc.pl",
    "&8[&c&lClash&f&lMC.pl&8] &7NameMC&8: &enamemc.clashmc.pl",
    "&8[&c&lClash&f&lMC.pl&8] &7Statystyki, topki oraz sklep znajdziesz na stronie&8: &ewww.clashmc.pl",
    "&8[&c&lClash&f&lMC.pl&8] &7Chcesz się z kimś wymienić? Użyj komendy&8: &e/wymiana &8(&7Musisz być na spawnie&8)",
    "&8[&c&lClash&f&lMC.pl&8] &7Jeżeli chcesz pozbyć się zbędnych przedmiotów, użyj komendy&8: &e/smietnik &8(&7Musisz być na spawnie&8)"
  };
}
