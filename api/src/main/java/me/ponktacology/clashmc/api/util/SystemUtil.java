package me.ponktacology.clashmc.api.util;

import com.sun.management.OperatingSystemMXBean;
import lombok.experimental.UtilityClass;

import java.lang.management.ManagementFactory;

@UtilityClass
public class SystemUtil {

    public double getCPUUsageProcess() {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        return osBean.getProcessCpuLoad();
    }

    public double getCPUUsageSystem() {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        return osBean.getSystemCpuLoad();
    }

    //In megabytes
    public double getFreeRam() {
        return Runtime.getRuntime().freeMemory() / (1024.0 * 1024.0);
    }

    //In megabytes
    public double getMaxRam() {
        return Runtime.getRuntime().totalMemory() / (1024.0 * 1024.0) ;
    }
}
