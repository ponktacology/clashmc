package me.ponktacology.clashmc.guild.nametag;



import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class NametagThread extends Thread {
    private static final Map<NametagUpdate, Boolean> pendingUpdates=new ConcurrentHashMap<>();

    public NametagThread() {
        super("qLib - Nametag Thread");
        this.setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            for (NametagUpdate pendingUpdate : NametagThread.pendingUpdates.keySet()) {
                try {
                    FrozenNametagHandler.applyUpdate(pendingUpdate);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(FrozenNametagHandler.getUpdateInterval() * 50L);
            }
            catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
    }

    
    public static Map<NametagUpdate, Boolean> getPendingUpdates() {
        return pendingUpdates;
    }
}

