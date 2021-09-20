package me.ponktacology.clashmc.auth.player.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.auth.AuthPermissions;
import me.ponktacology.clashmc.auth.player.AuthPlayer;
import me.ponktacology.clashmc.auth.player.cache.AuthPlayerCache;
import me.ponktacology.clashmc.auth.util.BungeeUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Name;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@RequiredArgsConstructor
public class AuthCommand {

  private final AuthPlayerCache playerCache;

  @Command(
      value = {"register", "r"},
      description = "Komenda do rejestracji",
      async = true)
  public void register(
      @Sender Player sender,
      @Name("password") String password,
      @Name("password") String password2) {
    Optional<AuthPlayer> authPlayerOptional = this.playerCache.get(sender);

    if (!authPlayerOptional.isPresent()) {
      return;
    }

    AuthPlayer authPlayer = authPlayerOptional.get();

    if (authPlayer.isRegistered()) {
      sender.sendMessage(
          Text.colored(
              "&cJesteś już zarejestrowany. Zaloguj się używając komendy &f/login <hasło>&c."));
      return;
    }

    if (!password.equals(password2)) {
      sender.sendMessage(Text.colored("&cHasła nie są takie same."));
      return;
    }

    if (password.length() < 4 || password.length() > 16) {
      sender.sendMessage(Text.colored("&cHasło musi mieć od 4 do 16 znaków."));
      return;
    }

    authPlayer.register(password);
    authPlayer.save();
    sender.sendMessage(Text.colored("&aZostałeś pomyślnie zarejestrowany."));
    BungeeUtil.sendToServer(sender, "lobby");
  }

  @Command(
      value = {"login", "l"},
      description = "Komenda do logowania",
      async = true)
  public void login(@Sender Player sender, @Name("password") String password) {
    Optional<AuthPlayer> authPlayerOptional = this.playerCache.get(sender);

    if (!authPlayerOptional.isPresent()) {
      return;
    }

    AuthPlayer authPlayer = authPlayerOptional.get();

    if (!authPlayer.isRegistered()) {
      sender.sendMessage(
          Text.colored(
              "&cNie jesteś zarejestrowany. Zarejestruj się używając komendy &f/register <hasło> <hasło>&c."));
      return;
    }

    if (!authPlayer.isPasswordCorrect(password)) {
      sender.sendMessage(Text.colored("&cBłędne hasło."));
      return;
    }

    authPlayer.setLoggedIn(true);
    authPlayer.save();
    sender.sendMessage(Text.colored("&aPomyślnie zalogowano."));
    BungeeUtil.sendToServer(sender, "lobby");
  }

  @Command(
      value = {"resetpassword", "resetpw"},
      description = "Resetuje hasło gracza",
      async = true)
  @Permission(AuthPermissions.RESET_PASSWORD)
  public void resetPassword(
      @Sender ConsoleCommandSender sender, @Name("player") AuthPlayer authPlayer) {
    authPlayer.resetPassword();
    authPlayer.save();
    sender.sendMessage(Text.colored("&aPomyślnie zresetowano hasło tego gracza."));
  }
}
