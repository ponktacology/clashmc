package me.ponktacology.clashmc.itemshop.task;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import org.bukkit.Bukkit;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;

@Slf4j
public class ItemShopFetchTask implements Runnable {

  private final String itemShopMysqlUrl;
  private final TaskDispatcher taskDispatcher;

  @SneakyThrows
  public ItemShopFetchTask(String itemShopMysqlUrl, TaskDispatcher taskDispatcher) {
    this.itemShopMysqlUrl = itemShopMysqlUrl;
    this.taskDispatcher = taskDispatcher;
    Class.forName("com.mysql.jdbc.Driver");
  }

  @Override
  public void run() {
    this.fetch();
  }

  private void fetch() {
    log.info("Fetching data....");
    fetchPlayers();
  }

  @SneakyThrows
  private Connection getConnection() {
    return DriverManager.getConnection(this.itemShopMysqlUrl);
  }

  private void executeCommands( String nickname, int amount,  List<String> commands) {
    for (String command : commands) {
      command = command.replace("{NICKNAME}", nickname).replace("{AMOUNT}", String.valueOf(amount));
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }
  }

  @SneakyThrows
  private void fetchPlayers() {
    try (PreparedStatement statement =
        this.getConnection().prepareStatement("SELECT * FROM cms_payments WHERE status = ?")) {

      statement.setString(1, "OK");

      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        String offer = String.valueOf(resultSet.getInt("shop"));
        String nickname = resultSet.getString("nickname");
        int amount = resultSet.getInt("amount");

        this.taskDispatcher.run(
            () -> this.executeCommands(nickname, amount, this.getCommands(offer)));
      }
    }

    try (PreparedStatement update =
        this.getConnection()
            .prepareStatement("UPDATE cms_payments SET status = ? WHERE status = ?")) {

      update.setString(1, "OK_AND_COMPLETED");
      update.setString(2, "OK");

      log.info(
          "Fetched itemshop and applied for " + update.executeUpdate() + " players.");
    }
  }


  @SneakyThrows
  private List<String> getCommands(String offer) {

    try (PreparedStatement statement =
        this.getConnection().prepareStatement("SELECT * FROM cms_shop WHERE id = ?")) {
      statement.setString(1, offer);

      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {

        List<String> commands = Lists.newArrayList();

        new Gson()
            .fromJson(resultSet.getString("commands"), JsonArray.class)
            .forEach(it -> commands.add(it.getAsString()));

        return commands;
      }
    }

    return Collections.emptyList();
  }
}
