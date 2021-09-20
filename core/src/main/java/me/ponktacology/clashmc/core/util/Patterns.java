package me.ponktacology.clashmc.core.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class Patterns {

  public static final Pattern ALPHA_NUMERIC = Pattern.compile("^[a-zA-Z0-9]*$");
  public static final Pattern DISALLOWED_URL =  Pattern.compile("^(http://www\\.|https://www\\.|http://|https://)?(?!google|youtube|twitch|youtu)[a-z0-9]+([\\-.][a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(/.*)?$");
  public static final Pattern IP = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])([.,])){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
}
