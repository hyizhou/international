package xyz.yizhou.monitor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import xyz.yizhou.monitor.bean.HistoryMemory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 将系统信息相关对象放入spring容器
 * @author huanggc
 * @date 2021/10/12 15:37
 */
@Configuration
public class SystemInfoConfig {
    private final SystemInfo systemInfo = new SystemInfo();

    @Bean
    public GlobalMemory memory(){
        HWDiskStore[] diskStores = systemInfo.getHardware().getDiskStores();
        return systemInfo.getHardware().getMemory();
    }

    @Bean
    public CentralProcessor cpu(){
        return systemInfo.getHardware().getProcessor();
    }

    /**
     * 磁盘，可能会有多个，因此使用list来存储
     * @return
     */
    @Bean
    public List<HWDiskStore> disk(){
        return new ArrayList<>(Arrays.asList(systemInfo.getHardware().getDiskStores()));
    }

    /**
     * 硬件信息，如bios，主板等信息
     * @return
     */
    @Bean
    public ComputerSystem computerSystem(){
        return systemInfo.getHardware().getComputerSystem();
    }

    @Bean
    public HistoryMemory historyMemory(){
        return new HistoryMemory(memory());
    }
}
