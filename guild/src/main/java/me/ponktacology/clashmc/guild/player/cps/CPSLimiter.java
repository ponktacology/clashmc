package me.ponktacology.clashmc.guild.player.cps;

import lombok.Setter;
import me.ponktacology.clashmc.guild.util.EvictingList;

@Setter
public class CPSLimiter {

    private final EvictingList<Long> delays = new EvictingList<>(20);
    private double lastFlag = -1L;

    private long lastCheck = System.currentTimeMillis();
    private double threshold = 0.0;

    private double incrementThreshold() {
        return ++threshold;
    }

    private void decrementThreshold() {
        threshold = Math.max(0, threshold - 0.2);
    }

   public boolean check() {
        long delay = System.currentTimeMillis() - lastCheck;
        lastCheck = System.currentTimeMillis();

        delays.add(delay);

        double avg = delays.stream().mapToDouble(it -> it).average().orElse(100);

        if (delays.size() >= 17) {
            if (avg < 64) {
                return incrementThreshold() >= 7;
            } else decrementThreshold();
        }

        return false;
    }

    public void resetLastFlag() {
        this.lastFlag = System.currentTimeMillis();
    }
}
