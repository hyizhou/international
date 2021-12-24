package xyz.yizhou.monitor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.FileSystem;
import oshi.software.os.NetworkParams;
import oshi.software.os.OperatingSystem;
import xyz.yizhou.monitor.bean.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 将系统信息相关对象放入spring容器
 *
 * @author hyizhou
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
        return Arrays.asList(systemInfo.getHardware().getDiskStores());
    }

    /**
     * 硬件信息，如bios，主板等信息
     */
    @Bean
    public ComputerSystem computerSystem() {
        return systemInfo.getHardware().getComputerSystem();
    }

    /**
     * 操作系统
     */
    @Bean
    public OperatingSystem operatingSystem(){
        return systemInfo.getOperatingSystem();
    }

    /**
     * 网卡信息
     */
    @Bean
    public List<NetworkIF> network() {
        return new ArrayList<>(Arrays.asList(systemInfo.getHardware().getNetworkIFs()));
    }

    /**
     * 系统层面网络信息，如ip地址，DNS等
     */
    @Bean
    public NetworkParams networkParams(){
        return systemInfo.getOperatingSystem().getNetworkParams();
    }

    /**
     * 文件系统对象，可得到分区信息
     */
    @Bean
    public FileSystem osFileStore(){
        return systemInfo.getOperatingSystem().getFileSystem();
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

    /**
     * 文件系统（可看成分区）历史信息
     */
    @Bean
    public HistoryFileSystem historyFileSystem(){
        return new HistoryFileSystem(osFileStore());
    }

    /**
     * 网络系统历史信息
     */
    @Bean
    public HistoryNet historyNet(){
        return new HistoryNet(network());
    }
}
