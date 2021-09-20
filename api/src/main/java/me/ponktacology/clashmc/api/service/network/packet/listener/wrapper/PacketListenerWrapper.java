package me.ponktacology.clashmc.api.service.network.packet.listener.wrapper;

import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.network.packet.Packet;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.api.settings.updater.SettingsUpdater;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class PacketListenerWrapper {

  private final Map<Class<? extends Packet>, Set<Method>> methods = new ConcurrentHashMap<>();
  private final PacketListener instance;

  public PacketListenerWrapper(PacketListener listener) {
    this.instance = listener;

    registerMethods();
  }

  public void handle(Packet packet) {
    Set<Method> packetMethods = methods.getOrDefault(packet.getClass(), Collections.emptySet());

    for (Method method : packetMethods) {
      try {
        method.invoke(instance, packet);
      } catch (ReflectiveOperationException e) {
        log.debug("Packet not handled correctly, packet= " + packet.getClass().getSimpleName());
        e.printStackTrace();
      }
    }
  }

  private void addMethod(Class<? extends Packet> clazz, Method method) {
    Set<Method> methods = this.methods.getOrDefault(clazz, new HashSet<>());
    methods.add(method);
    this.methods.put(clazz, methods);
  }

  public void registerMethods() {
    for (Method[] methodArr :
        new Method[][] {
          this.instance.getClass().getDeclaredMethods(), this.instance.getClass().getMethods()
        }) {
      for (Method method : methodArr) {
        if (!method.isAnnotationPresent(PacketHandler.class)) {
          continue;
        }

        if (this.instance instanceof SettingsUpdater) {
          SettingsUpdater<?, ?> updater = (SettingsUpdater<?, ?>) this.instance;
          addMethod(updater.getPacketClazz(), method);
          break;
        }

        Optional<Class<? extends Packet>> optionalClass = getPacket(method);

        if (!optionalClass.isPresent()) {
          continue;
        }

        addMethod(optionalClass.get(), method);
      }
    }
  }

  private Optional<Class<? extends Packet>> getPacket(Method method) {
    Class<?> parameterType = method.getParameterTypes()[0];

    if (!Packet.class.isAssignableFrom(parameterType)) {
      return Optional.empty();
    }

    return Optional.of((Class<? extends Packet>) parameterType);
  }

  public Set<Class<? extends Packet>> getPackets() {
    return this.methods.keySet();
  }
}
