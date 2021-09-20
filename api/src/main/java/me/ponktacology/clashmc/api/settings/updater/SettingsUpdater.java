package me.ponktacology.clashmc.api.settings.updater;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.api.settings.Settings;
import me.ponktacology.clashmc.api.settings.cache.SettingsCache;
import me.ponktacology.clashmc.api.settings.factory.SettingsFactory;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;

@RequiredArgsConstructor
public abstract class SettingsUpdater<V extends Settings, P extends PacketUpdate<V>>
    implements Updater, PacketListener {

  protected final DataService dataService;
  protected final NetworkService networkService;
  @Getter private final Class<P> packetClazz; // this needs to have default constructor
  protected final SettingsCache<V> settingsCache;
  protected final SettingsFactory<V> settingsFactory;

  @SneakyThrows
  public void update(V entity) {
    entity.save(this.dataService);
    this.networkService.publish(this.packetClazz.newInstance());
  }

  public void update() {
    V entity = this.settingsCache.get();
    this.update(entity);
  }

  @PacketHandler
  public void onPacket(P packet) {
    V settings = this.settingsFactory.loadOrCreate();

    this.settingsCache.set(settings);
  }
}
