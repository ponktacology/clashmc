package me.ponktacology.clashmc.guild.guild.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.api.util.MathUtil;
import me.ponktacology.clashmc.api.util.TimeUtil;
import me.ponktacology.clashmc.core.CorePermissions;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.menu.menus.ConfirmMenu;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.InventoryUtil;
import me.ponktacology.clashmc.core.util.Patterns;
import me.ponktacology.clashmc.core.util.StyleUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.GuildConstants;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.GuildRole;
import me.ponktacology.clashmc.guild.guild.action.ActionType;
import me.ponktacology.clashmc.guild.guild.action.GuildAction;
import me.ponktacology.clashmc.guild.guild.action.announcer.GuildActionAnnouncer;
import me.ponktacology.clashmc.guild.guild.cache.GuildCache;
import me.ponktacology.clashmc.guild.guild.factory.GuildFactory;
import me.ponktacology.clashmc.guild.guild.invite.GuildInvite;
import me.ponktacology.clashmc.guild.guild.invite.updater.InviteUpdater;
import me.ponktacology.clashmc.guild.guild.leave.GuildLeave;
import me.ponktacology.clashmc.guild.guild.leave.updater.LeaveUpdater;
import me.ponktacology.clashmc.guild.guild.message.GuildMessage;
import me.ponktacology.clashmc.guild.guild.message.announcer.GuildMessageAnnouncer;
import me.ponktacology.clashmc.guild.guild.panel.GuildPanelMenu;
import me.ponktacology.clashmc.guild.guild.permission.GuildPermission;
import me.ponktacology.clashmc.guild.guild.settings.GuildSettings;
import me.ponktacology.clashmc.guild.guild.settings.cache.GuildSettingsCache;
import me.ponktacology.clashmc.guild.guild.settings.menu.GuildItemsMenu;
import me.ponktacology.clashmc.guild.guild.updater.GuildUpdater;
import me.ponktacology.clashmc.guild.guild.war.GuildWar;
import me.ponktacology.clashmc.guild.guild.war.updater.GuildWarUpdater;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.util.GuildItemsUtil;
import me.ponktacology.clashmc.guild.util.GuildRegionUtil;
import me.ponktacology.clashmc.sector.SectorPlugin;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.player.teleport.request.LocationTeleportRequest;
import me.ponktacology.clashmc.sector.player.teleport.updater.PlayerTeleportUpdater;
import me.vaperion.blade.command.annotation.*;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GuildCommand {

  private final GuildSettingsCache guildSettingsCache;
  private final GuildPlayerCache playerCache;
  private final GuildFactory guildFactory;
  private final GuildCache guildCache;
  private final GuildUpdater guildUpdater;
  private final GuildActionAnnouncer guildActionAnnouncer;
  private final InviteUpdater inviteUpdater;
  private final LeaveUpdater leaveUpdater;
  private final GuildMessageAnnouncer guildMessageAnnouncer;
  private final PlayerTeleportUpdater teleportUpdater;
  private final CorePlayerCache corePlayerCache;
  private final TaskDispatcher taskDispatcher;
  private final GuildWarUpdater warUpdater;

  @Command(value = "gdebug")
  @Permission(CorePermissions.DEV)
  public void debug(@Sender CommandSender sender, @Name("guild") Guild guild) {
    sender.sendMessage(guild.toString());
  }

  @Command(value = {"g", "gildie", "gildia", "g help", "g pomoc"})
  public void help(@Sender CommandSender sender) {
    Arrays.asList(
            "&e/dg <wiadomo????> &8- &7Wiadomo???? do gildii&8.",
            "&e/g dolacz <tag gildii> &8- &7Do????cza do gildii&8.",
            "&e/g dom &8- &7Teleportuje na dom gildii&8.",
            "&e/ff &8- &7W????cza lub wy????cza PVP w gildii&8.",
            "&e/g info <tag gildii> &8- &7Informacje o danej gildii&8.",
            "&e/g opusc &8- &7Opuszcza gildi??&8.",
            "&e/g panel &8- &7 Panel zarz??dzania gildi??&8.",
            "&e/g ustawdom &8- &7Ustawia dom gildijny&8.",
            "&e/g usun &8- &7Usuwa twoj?? gildi??&8.",
            "&e/g wojna <tag gildii> &8- &7Wypowiada wojn?? innej gildii&8.",
            "&e/g wyrzuc <gracz> &8- &7Wyrzuca gracza z gildii&8.",
            "&e/g zapros <gracz> &8- &7Zaprasza gracza do gildii&8.",
            "&e/g zaloz <tag> <nazwa gildii> &8- &7Zak??ada now?? gildi??&8.",
            "&e/g zaproswszystkich &8- &7Zaprasza wszystkich graczy w odleg??o??ci 5 kratek do gildii&8.")
        .forEach(it -> sender.sendMessage(Text.colored(it)));
  }

  @Command(value = "g itemy", description = "Pokazuje list?? item??w na gildie")
  public void items(@Sender Player sender) {
    GuildSettings settings = this.guildSettingsCache.get();

    if (!settings.isEnabledItems()) {
      sender.sendMessage(Text.colored("&cPodgl??d item??w na gildie jest aktualnie wy????czony."));
      return;
    }

    new GuildItemsMenu().openMenu(sender);
  }

  @Command(value = "g zaloz", description = "Zak??ada now?? gildi??", async = true)
  public void create(@Sender Player sender, @Name("tag") String tag, @Name("name") String name) {
    GuildSettings settings = this.guildSettingsCache.get();

    if (!settings.isEnabled()) {
      sender.sendMessage(Text.colored("&cGildie s?? aktualnie wy????czone."));
      return;
    }

    Sector localSector = SectorPlugin.INSTANCE.getLocalSector();

    if (localSector.isSpawn()) {
      sender.sendMessage(Text.colored("&cNie mo??esz za??o??y?? gildii na spawnie."));
      return;
    }

    GuildPlayer guildPlayer = this.playerCache.getOrKick(sender);

    if (guildPlayer.hasGuild()) {
      sender.sendMessage(Text.colored("&cJeste?? ju?? w gildii."));
      return;
    }

    if (!MathUtil.isBetween(
        tag.length(), GuildConstants.MIN_TAG_LENGTH, GuildConstants.MAX_TAG_LENGTH)) {
      sender.sendMessage(
          Text.colored(
              "&cTag gildii musi mie?? minimalnie od "
                  + GuildConstants.MIN_TAG_LENGTH
                  + " do "
                  + GuildConstants.MAX_TAG_LENGTH
                  + " znak??w."));
      return;
    }

    if (!MathUtil.isBetween(
        name.length(), GuildConstants.MIN_NAME_LENGTH, GuildConstants.MAX_NAME_LENGTH)) {
      sender.sendMessage(
          Text.colored(
              "&cNazwa gildii musi mie?? minimalnie od "
                  + GuildConstants.MIN_NAME_LENGTH
                  + " do "
                  + GuildConstants.MAX_NAME_LENGTH
                  + " znak??w."));
      return;
    }

    if (!Patterns.ALPHA_NUMERIC.matcher(tag).matches()) {
      sender.sendMessage(Text.colored("&cTag gildii musi by?? alfanumeryczny."));
      return;
    }

    if (!Patterns.ALPHA_NUMERIC.matcher(name).matches()) {
      sender.sendMessage(Text.colored("&cNazwa gildii musi by?? alfanumeryczna."));
      return;
    }

    if (this.guildCache.getByTag(tag).isPresent()) {
      sender.sendMessage(Text.colored("&cGildia z takim tagiem ju?? istnieje."));
      return;
    }

    if (this.guildCache.getByName(name).isPresent()) {
      sender.sendMessage(Text.colored("&cGildia z tak?? nazw?? ju?? istnieje."));
      return;
    }

    if (!GuildRegionUtil.availableForGuildCreation(sender, sender.getLocation())) {
      return;
    }

    CorePlayer corePlayer = this.corePlayerCache.getOrKick(sender);

    if (!(corePlayer.isStaff() || GuildItemsUtil.hasEnoughItems(guildPlayer))) {
      sender.sendMessage(Text.colored("&cNie masz wymaganych przedmiot??w do za??o??enia gildii."));
      return;
    }

    double multiplier = GuildItemsUtil.guildItemsMultiplier(corePlayer);
    List<ItemStack> items = settings.getItems(multiplier);

    InventoryUtil.removeItems(sender, items);

    Location senderLocation = sender.getLocation().getBlock().getLocation().clone();
    Guild guild = this.guildFactory.create(tag, name, sender, senderLocation);
    guildPlayer.setGuild(guild);
    guildPlayer.save();

    this.guildUpdater.update(guild);
    this.taskDispatcher.runLater(() -> sender.teleport(guild.getCenter()), 1, TimeUnit.SECONDS);
    this.taskDispatcher.runLaterAsync(
        () ->
            this.guildActionAnnouncer.announce(
                new GuildAction(ActionType.CREATE, guildPlayer, guild)),
        1L,
        TimeUnit.SECONDS);
  }

  @Command(value = "g usun", description = "Usuwa twoj?? gildi??", async = true)
  public void remove(@Sender Player sender) {
    Optional<GuildPlayer> guildPlayerOptional = this.playerCache.get(sender);

    if (!guildPlayerOptional.isPresent()) return;

    GuildPlayer guildPlayer = guildPlayerOptional.get();

    if (!guildPlayer.hasGuild()) {
      sender.sendMessage(Text.colored("&cMusisz by?? w gildii."));
      return;
    }

    Guild guild = guildPlayer.getGuild().get();

    if (guild.getRole(guildPlayer) != GuildRole.LEADER) {
      sender.sendMessage(Text.colored("&cMusisz by?? liderem, aby usun???? gildi??."));
      return;
    }

    new ConfirmMenu(
            "&ePotwierd?? usuni??cie gildii",
            result -> {
              if (result) {
                this.guildUpdater.remove(guild);
                sender.sendMessage(Text.colored("&aPomy??lnie usuni??to gild??."));
                guildPlayer.save();
              }
            },
            true)
        .openMenu(sender);
  }

  /* @Command(value = "g powieksz", description = "Powi??ksza teren gildii", async = true)
  public void enlarge(@Sender Player sender) {
    GuildPlayer senderGuildPlayer = this.playerCache.getOrKick(sender);

    if (!senderGuildPlayer.hasGuild()) {
      sender.sendMessage(Text.colored("&cMusisz by?? w gildii."));
      return;
    }

    Guild guild = senderGuildPlayer.getGuild().get();

    if (!guild.hasPermission(senderGuildPlayer, GuildPermission.Permissions.ENLARGE_GUILD)) {
      sender.sendMessage(
          Text.colored(
              "&cNie posiadasz permisji do powi??kszania terenu gildii. Popro?? lidera o zmian?? ich w /g panel."));
      return;
    }

    if (guild.getRegionSize() == GuildConstants.MAX_GUILD_REGION_SIZE) {
      sender.sendMessage(Text.colored("&cTwoja gildia osi??gn????a ju?? maksymalny rozmiar terenu."));
      return;
    }

    int priceInBlocks = guild.getRegionSize() * 2;

    if (!InventoryUtil.hasItem(sender, Material.EMERALD_BLOCK, priceInBlocks)) {
      sender.sendMessage(
          Text.colored(
              "&cPotrzebujesz "
                  + priceInBlocks
                  + " blok??w szmaragdu, aby powi??kszy?? teren gildii."));
      return;
    }

    InventoryUtil.removeItem(sender, Material.EMERALD_BLOCK, priceInBlocks);

    guild.enlarge();
    this.guildUpdater.update(guild);
    sender.sendMessage(Text.colored("&aPomy??lnie powi??kszono teren gildii."));
  } */

  @Command(value = "g zapros", description = "Zaprasza gracza do gildii", async = true)
  public void invite(@Sender Player sender, @Name("player") GuildPlayer guildPlayer) {
    Optional<GuildPlayer> senderGuildPlayerOptional = this.playerCache.get(sender);

    if (!senderGuildPlayerOptional.isPresent()) return;

    GuildPlayer senderGuildPlayer = senderGuildPlayerOptional.get();

    if (!senderGuildPlayer.hasGuild()) {
      sender.sendMessage(Text.colored("&cMusisz by?? w gildii."));
      return;
    }

    Guild guild = senderGuildPlayer.getGuild().get();

    if (!guild.hasPermission(senderGuildPlayer, GuildPermission.Permissions.INVITE_MEMBER)) {
      sender.sendMessage(
          Text.colored(
              "&cNie posiadasz permisji do zapraszania graczy. Popro?? lidera o zmian?? ich w /g panel."));
      return;
    }

    if (guildPlayer.hasGuild()) {
      sender.sendMessage(Text.colored("&cTen gracz jest ju?? w gildii."));
      return;
    }

    if (this.guildSettingsCache.get().getMaxMembers() <= guild.members().size()) {
      sender.sendMessage(Text.colored("&cTwoja gildia posiada ju?? maksymaln?? ilo???? cz??onk??w."));
      return;
    }

    this.inviteUpdater.update(new GuildInvite(guildPlayer, guild));
    sender.sendMessage(Text.colored("&aPomy??lnie wys??ano zaproszenie do gildii."));
  }

  @Command(
      value = "g zaproswszystkich",
      description = "Zaprasza wszystkich graczy w odgleglosci 5 kratek do gildii",
      async = true)
  public void inviteAll(@Sender Player sender) {
    Optional<GuildPlayer> senderGuildPlayerOptional = this.playerCache.get(sender);

    if (!senderGuildPlayerOptional.isPresent()) return;

    GuildPlayer senderGuildPlayer = senderGuildPlayerOptional.get();

    if (!senderGuildPlayer.hasGuild()) {
      sender.sendMessage(Text.colored("&cMusisz by?? w gildii."));
      return;
    }

    Guild guild = senderGuildPlayer.getGuild().get();

    if (!guild.hasPermission(senderGuildPlayer, GuildPermission.Permissions.INVITE_MEMBER)) {
      sender.sendMessage(
          Text.colored(
              "&cNie posiadasz permisji do zapraszania graczy. Popro?? lidera o zmian?? ich w /g panel."));
      return;
    }

    for (Entity entity : sender.getNearbyEntities(5, 5, 5)) {
      if (!(entity instanceof Player)) continue;

      Player player = (Player) entity;
      Optional<GuildPlayer> guildPlayerOptional = this.playerCache.get(player);

      if (!guildPlayerOptional.isPresent()) {
        continue;
      }

      GuildPlayer guildPlayer = guildPlayerOptional.get();

      if (guildPlayer.hasGuild()) {
        continue;
      }

      this.inviteUpdater.update(new GuildInvite(guildPlayer, guild));
    }

    sender.sendMessage(Text.colored("&aPomy??lnie wys??ano zaproszenia do gildii."));
  }

  @Command(value = "g dolacz", description = "Do????cza do gildii", async = true)
  public void join(@Sender Player sender, @Name("guild") Guild guild) {
    Optional<GuildPlayer> senderGuildPlayerOptional = this.playerCache.get(sender);

    if (!senderGuildPlayerOptional.isPresent()) return;

    GuildPlayer senderGuildPlayer = senderGuildPlayerOptional.get();

    if (senderGuildPlayer.hasGuild()) {
      sender.sendMessage(Text.colored("&cJeste?? ju?? w gildii."));
      return;
    }

    if (!guild.isInvited(senderGuildPlayer)) {
      sender.sendMessage(
          Text.colored("&cNie otrzyma??e?? zaproszenia do tej gildii lub ono wygas??o."));
      return;
    }

    if (this.guildSettingsCache.get().getMaxMembers() <= guild.members().size()) {
      sender.sendMessage(Text.colored("&cTa gildia posiada ju?? maksymaln?? ilo???? cz??onk??w."));
      return;
    }

    this.inviteUpdater.update(new GuildInvite(senderGuildPlayer, guild, true));
    sender.sendMessage(Text.colored("&aPomy??lnie do????czy??e?? do gildii."));
  }

  @Command(value = "g wyrzuc", description = "Wyrzuca gracza z gildii", async = true)
  public void kick(@Sender Player sender, @Name("player") GuildPlayer guildPlayer) {
    Optional<GuildPlayer> senderGuildPlayerOptional = this.playerCache.get(sender);

    if (!senderGuildPlayerOptional.isPresent()) return;

    GuildPlayer senderGuildPlayer = senderGuildPlayerOptional.get();

    if (!senderGuildPlayer.hasGuild()) {
      sender.sendMessage(Text.colored("&cMusisz by?? w gildii."));
      return;
    }

    Guild guild = senderGuildPlayer.getGuild().get();

    if (!guild.hasPermission(senderGuildPlayer, GuildPermission.Permissions.KICK_MEMBER)) {
      sender.sendMessage(
          Text.colored(
              "&cNie posiadasz permisji do wyrzucania graczy. Popro?? lidera o zmian?? ich w /g panel."));
      return;
    }

    if (guild.getRole(guildPlayer) == GuildRole.LEADER) {
      sender.sendMessage(Text.colored("&cNie mo??esz wyrzuci?? lidera gildii."));
      return;
    }

    if (!guild.equals(guildPlayer.getGuild().orElse(null))) {
      sender.sendMessage(Text.colored("&cTen gracz nie jest w twojej gildii."));
      return;
    }

    this.leaveUpdater.update(new GuildLeave(guildPlayer, guild, true));
    sender.sendMessage(Text.colored("&aPomy??lnie wyrzucono gracza z gildii."));
  }

  @Command(value = "g opusc", description = "Opuszcza gildi??", async = true)
  public void leave(@Sender Player sender) {
    Optional<GuildPlayer> senderGuildPlayerOptional = this.playerCache.get(sender);

    if (!senderGuildPlayerOptional.isPresent()) return;

    GuildPlayer senderGuildPlayer = senderGuildPlayerOptional.get();

    if (!senderGuildPlayer.hasGuild()) {
      sender.sendMessage(Text.colored("&cMusisz by?? w gildii."));
      return;
    }

    Guild guild = senderGuildPlayer.getGuild().get();

    if (guild.getRole(senderGuildPlayer) == GuildRole.LEADER) {
      sender.sendMessage(
          Text.colored(
              "&cMusisz przekaza?? rol?? lidera innemu graczowi zanim opu??cisz gildi?? lub j?? usun????."));
      return;
    }

    this.leaveUpdater.update(new GuildLeave(senderGuildPlayer, guild, false));
    sender.sendMessage(Text.colored("&aPomy??lnie opuszczono gildi??."));
  }

  @Command(value = "g info", description = "Informacje o danej gildii", async = true)
  public void info(
      @Sender CommandSender sender,
      @Name("guild") @me.vaperion.blade.command.annotation.Optional("DEFAULT_VALUE") Guild guild) {
    GuildPlayer leader = guild.leader();
    List<GuildPlayer> members = guild.members();
    Set<GuildPlayer> onlineMembers =
        members.parallelStream()
            .filter(it -> CorePlugin.INSTANCE.getPlayerCache().isOnlineNotInAuthOrLobby(it))
            .collect(Collectors.toSet());
    String formattedMembers =
        members.stream()
            .sorted(
                (o1, o2) -> {
                  int value = -(guild.getRole(o1).getPower() - guild.getRole(o2).getPower());

                  if (value == 0) {
                    return -(o1.getRank() - o2.getRank());
                  }

                  return value;
                })
            .map(it -> (onlineMembers.contains(it) ? "" : "&7") + it.getName())
            .collect(Collectors.joining("&f, "));

    long onlineMembersSize = onlineMembers.size();

    String allies =
        guild.allies().isEmpty()
            ? "Brak"
            : guild.allies().stream().map(Guild::getTag).collect(Collectors.joining("&f, "));
    Arrays.asList(
            "",
            "&eInformacje dotycz??ce gildii &f".concat(guild.getTag()),
            " &f* &7Lider: &f".concat(leader.getName()),
            " &f* &7??ycia: &f" + guild.getLives(),
            " &f* &7Ranking: &f" + guild.rank(),
            " &f* &7Pozycja w rankingu: &f" + this.guildCache.getPosition(guild),
            " &f* &7Zab??jstwa: &f" + guild.kills(),
            " &f* &7??mierci: &f" + guild.deaths(),
            " &f* &7KillStreak: &f" + guild.killStreak(),
            " &f* &7Koordynaty: &fX: " + guild.centerX() + " Z: " + guild.centerZ(),
            " &f* &7Cz??onkowie ("
                + onlineMembersSize
                + "/"
                + members.size()
                + "): &f"
                + formattedMembers
                + (this.guildSettingsCache.get().isEnabledAllies()
                    ? "\n&eSojusznicy: &f" + allies
                    : ""),
            " &f* &7Prowadzone wojny: &f"
                + (guild.wars().stream()
                    .map(
                        it ->
                            "&c"
                                + it.getEnemy().getTag()
                                + " &7"
                                + TimeUtil.formatTimeMillis(it.getTimeLeft()))
                    .collect(Collectors.joining("&f, "))))
        .forEach(it -> sender.sendMessage(Text.colored(it)));
  }

  @Command(value = "g ustawdom", description = "Ustawia dom gildyjny", async = true)
  public void setBase(@Sender Player sender) {
    Optional<GuildPlayer> senderGuildPlayerOptional = this.playerCache.get(sender);

    if (!senderGuildPlayerOptional.isPresent()) return;

    GuildPlayer senderGuildPlayer = senderGuildPlayerOptional.get();

    if (!senderGuildPlayer.hasGuild()) {
      sender.sendMessage(Text.colored("&cMusisz by?? w gildii."));
      return;
    }

    Guild guild = senderGuildPlayer.getGuild().get();

    if (!guild.hasPermission(senderGuildPlayer, GuildPermission.Permissions.BASE_LOCATION)) {
      sender.sendMessage(
          Text.colored(
              "&cNie posiadasz permisji do ustawienia lokalizacji bazy. Popro?? lidera o zmian?? ich w /g panel."));
      return;
    }

    guild.setBase(sender.getLocation().getBlock().getLocation());
    this.guildUpdater.update(guild);

    sender.sendMessage(Text.colored("&aPomy??lnie zmieni??e?? lokalizacje bazy."));
  }

  @Command(value = "g dom", description = "Teleportuje na dom gildyjny", async = true)
  public void base(@Sender Player sender) {
    Optional<GuildPlayer> senderGuildPlayerOptional = this.playerCache.get(sender);

    if (!senderGuildPlayerOptional.isPresent()) return;

    GuildPlayer senderGuildPlayer = senderGuildPlayerOptional.get();

    if (!senderGuildPlayer.hasGuild()) {
      sender.sendMessage(Text.colored("&cMusisz by?? w gildii."));
      return;
    }

    Guild guild = senderGuildPlayer.getGuild().get();

    CorePlayer corePlayer = this.corePlayerCache.getOrKick(sender);

    this.teleportUpdater.update(new LocationTeleportRequest(corePlayer, guild.getBase()));
  }

  @Command(
      value = {"g dg", "dg"},
      description = "Wiadomo???? do gildii sojuszniczych",
      async = true)
  public void messageMembers(@Sender Player sender, @Name("message") @Combined String message) {
    Optional<GuildPlayer> senderGuildPlayerOptional = this.playerCache.get(sender);

    if (!senderGuildPlayerOptional.isPresent()) return;

    GuildPlayer senderGuildPlayer = senderGuildPlayerOptional.get();

    if (!senderGuildPlayer.hasGuild()) {
      sender.sendMessage(Text.colored("&cMusisz by?? w gildii."));
      return;
    }

    this.guildMessageAnnouncer.announce(
        new GuildMessage(senderGuildPlayer, senderGuildPlayer.getGuild().get(), message),
        GuildMessage.MessageType.GUILD);
  }

  @Command(
      value = {"g friendlyfire", "ff", "friendlyfire"},
      description = "W????cza/Wy????cza PVP w gildii",
      async = true)
  public void friendlyFire(@Sender Player sender) {
    GuildPlayer senderGuildPlayer = this.playerCache.getOrKick(sender);

    if (!senderGuildPlayer.hasGuild()) {
      sender.sendMessage(Text.colored("&cMusisz by?? w gildii."));
      return;
    }

    Guild guild = senderGuildPlayer.getGuild().get();

    if (!guild.hasPermission(senderGuildPlayer, GuildPermission.Permissions.FRIENDLY_FIRE)) {
      sender.sendMessage(
          Text.colored(
              "&cNie posiadasz permisji do w????czania/wy????czania PvP w gildii. Popro?? lidera o zmian?? ich w /g panel."));
      return;
    }

    guild.toggleFriendlyFire();

    sender.sendMessage(
        Text.colored(
            "&aPomy??lnie " + StyleUtil.state(guild.isFriendlyFire()) + " &aPvP w gildii."));

    this.guildUpdater.update(guild);
  }

  /*
  @Command(
      value = {"g wolnemiejsce", "wolnemiejsce"},
      description = "Pokazuje koordynaty wolnego miejsca do za??o??enia gildii",
      async = true)
  public void freeSpace(@Sender Player sender) {
    Optional<GuildPlayer> senderGuildPlayerOptional = this.playerCache.get(sender);

    if (!senderGuildPlayerOptional.isPresent()) return;

    GuildPlayer senderGuildPlayer = senderGuildPlayerOptional.get();

    if (senderGuildPlayer.hasGuild()) {
      sender.sendMessage(Text.colored("&cJeste?? ju?? w gildii."));
      return;
    }

    boolean found = false;

    long start = System.currentTimeMillis();

    do {
      if (System.currentTimeMillis() - start > 5000L) {
        sender.sendMessage(Text.colored("&cNie znaleziono wolnego miejsca."));
        break;
      }

      int borderX = SectorConstants.BORDER;
      int borderZ = SectorConstants.BORDER;

      int x = MathUtil.random(-borderX, borderX);
      int z = MathUtil.random(-borderZ, borderZ);

      Location location = new Location(sender.getWorld(), x, 0, z);

      if (!GuildRegionUtil.availableForGuildCreation(sender, location, false)) {
        continue;
      }

      sender.sendMessage(Text.colored("&eWolne miejsce:&7 X: " + x + " Z: " + z));
      found = true;
    } while (!found);
  }
   */

  @Command(value = "g panel", description = "Otwiera panel gildyjny")
  public void panel(@Sender Player sender) {
    GuildPlayer senderGuildPlayer = this.playerCache.getOrKick(sender);

    if (!senderGuildPlayer.hasGuild()) {
      sender.sendMessage(Text.colored("&cMusisz by?? w gildii."));
      return;
    }

    Guild guild = senderGuildPlayer.getGuild().get();

    new GuildPanelMenu(guild).openMenu(sender);
  }

  @Command(
      value = {"g wojna"},
      description = "Wypowiada wojn?? gildii",
      async = true)
  public void war(@Sender Player sender, @Name("guild") Guild enemy) {
    if (!this.guildSettingsCache.get().isEnableWars()) {
      sender.sendMessage(Text.colored("&cWojny gildii s?? aktualnie wy????czone."));
      return;
    }

    GuildPlayer senderGuildPlayer = this.playerCache.getOrKick(sender);

    if (!senderGuildPlayer.hasGuild()) {
      sender.sendMessage(Text.colored("&cMusisz by?? w gildii."));
      return;
    }

    Guild guild = senderGuildPlayer.getGuild().get();

    if (!guild.hasPermission(senderGuildPlayer, GuildPermission.Permissions.DECLARE_WAR)) {
      sender.sendMessage(
          Text.colored(
              "&cNie posiadasz permisji do wypowiadania wojen. Popro?? lidera o zmian?? ich w /g panel."));
      return;
    }

    if (guild.equals(enemy)) {
      sender.sendMessage(Text.colored("&cNie mo??esz rozpocz???? wojny ze swoj?? gildi??."));
      return;
    }

    if (guild.isInWar(enemy)) {
      sender.sendMessage(Text.colored("&cProwadzisz ju?? wojn?? z t?? gildi??."));
      return;
    }

    if (guild.warsSize() >= GuildConstants.MAX_CONCURRENT_WARS) {
      sender.sendMessage(
          Text.colored(
              "&cTwoja gildia posiada ju?? maksymaln?? ilo???? wojen prowadzonych jednocze??nie."));
      return;
    }

    if (enemy.warsSize() >= GuildConstants.MAX_CONCURRENT_WARS) {
      sender.sendMessage(
          Text.colored(
              "&cTa gildia posiada ju?? maksymaln?? ilo???? wojen prowadzonych jednocze??nie."));
      return;
    }

    if (!guild.canStartWarAgainst(enemy)) {
      sender.sendMessage(
          Text.colored(
              "&cNast??pn?? wojn?? tej gildii mo??esz wypowiedzie?? za "
                  + TimeUtil.formatTimeMillis(guild.timeTillCanStartWar(enemy))));
      return;
    }

    this.warUpdater.update(new GuildWar(guild, enemy));
    sender.sendMessage(Text.colored("&aPomy??lnie rozpocz??to wojn??."));
  }
}
