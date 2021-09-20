package me.ponktacology.clashmc.core.player.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.service.network.redis.RedisDataService;
import me.ponktacology.clashmc.core.CorePermissions;
import me.ponktacology.clashmc.core.util.Text;
import me.vaperion.blade.command.annotation.*;
import org.bukkit.command.CommandSender;


@RequiredArgsConstructor
public class RedisCommand {


  private final RedisDataService redisDataService;

  private final RedisDataService cacheRedisDataService;

  @Command(value = "hset", async = true)
  @Permission(CorePermissions.DEV)
  public void hset(
           @Sender CommandSender sender,
          @Name("channel") String channel,
          @Name("key") String key,
          @Name("value") String value) {
    this.redisDataService.set(channel, key, value);
    sender.sendMessage(Text.colored("&aPomyślnie ustawiono wartość."));
  }

  @Command(value = "hget", async = true)
  @Permission(CorePermissions.DEV)
  public void hget(
           @Sender CommandSender sender, @Name("channel") String channel, @Name("key") String key) {
    sender.sendMessage(this.redisDataService.get(channel, key).orElse("null"));
  }

  @Command(value = "hdel", async = true)
  @Permission(CorePermissions.DEV)
  public void hdel(
           @Sender CommandSender sender,
          @Flag('c') boolean cache,
          @Name("channel") String channel,
          @Name("key") String key) {
    if (cache) {
      this.cacheRedisDataService.del(channel, key);
      sender.sendMessage(Text.colored("&aPomyślnie usunięto wartość."));
      return;
    }

    this.redisDataService.del(channel, key);
    sender.sendMessage(Text.colored("&aPomyślnie usunięto wartość."));
  }

  @Command(value = "redislocalcaching")
  @Permission(CorePermissions.DEV)
  public void execute(
           @Sender CommandSender sender, @Flag('c') boolean cache, @Name("enabled") boolean enabled) {
    if (cache) {
      this.cacheRedisDataService.setEnableCache(enabled);

      sender.sendMessage(
          Text.colored("&aPomyślnie zmieniono na " + cacheRedisDataService.isEnableCache()));
      return;
    }

    this.redisDataService.setEnableCache(enabled);

    sender.sendMessage(
        Text.colored("&aPomyślnie zmieniono na " + redisDataService.isEnableCache()));
  }
}
