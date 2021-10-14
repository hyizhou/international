package xyz.yizhou.monitor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.OSFileStore;
import xyz.yizhou.monitor.bean.HistoryCpu;
import xyz.yizhou.monitor.bean.HistoryDisk;
import xyz.yizhou.monitor.bean.HistoryMemory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 将系统信息相关对象放入spring容器
 *
 * @author huanggc
 * @date 2021/10/12 15:37
 */
@Configuration
public class SystemInfoConfig {
    private final SystemInfo systemInfo = new SystemInfo();

    @Bean
    public GlobalMemory memory() {
        HWDiskStore[] diskStores = systemInfo.getHardware().getDiskStores();
        return systemInfo.getHardware().getMemory();
    }

    @Bean
    public CentralProcessor cpu() {
        return systemInfo.getHardware().getProcessor();
    }

    /**
     * 磁盘，可能会有多个，因此使用list来存储
     */
    @Bean
    public List<HWDiskStore> disks() {
        return new ArrayList<>(Arrays.asList(systemInfo.getHardware().getDiskStores()));
    }

    /**
     * 硬件信息，如bios，主板等信息
     */
    @Bean
    public ComputerSystem computerSystem() {
        return systemInfo.getHardware().getComputerSystem();
    }

    /**
     * 网络信息
     */
    @Bean
    public List<NetworkIF> network() {
        return new ArrayList<>(Arrays.asList(systemInfo.getHardware().getNetworkIFs()));
    }

    /**
     * 分区、设备对象
     */
    @Bean
    public List<OSFileStore> osFileStore(){
        return new ArrayList<>(Arrays.asList(systemInfo.getOperatingSystem().getFileSystem().getFileStores()));
    }

    /**
     * 历史内存信息
     */
    @Bean
    public HistoryMemory historyMemory() {
        return new HistoryMemory(memory());
    }

    /**
     * 历史cpu信息
     */
    @Bean
    public HistoryCpu historyCpu() {
        systemInfo.getOperatingSystem().getFileSystem();
        return new HistoryCpu(cpu());
    }

    /**
     * 硬盘历史信息
     */
    @Bean
    public HistoryDisk historyDisk(){
        return new HistoryDisk(disks());
    }
}
