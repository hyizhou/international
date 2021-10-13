package xyz.yizhou.monitor.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import xyz.yizhou.monitor.bean.HistoryCpu;
import xyz.yizhou.monitor.bean.HistoryMemory;
import xyz.yizhou.monitor.bean.SnapshotCpu;

import java.util.Arrays;
import java.util.List;

/**
 * 用于测试的控制类
 * @author huanggc
 * @date 2021/10/12 15:18
 */
@RestController
@RequestMapping
public class TestControl {
    @Autowired
    private GlobalMemory memory;
    @Autowired
    private HistoryMemory hmemory;
    @Autowired
    private CentralProcessor cpu;
    @Autowired
    private HistoryCpu hcpu;
    @Autowired
    private List<HWDiskStore> disks;

    @GetMapping("/test")
    public String test(){
        return "<h1>Test</h1>";
    }

    @GetMapping("/test1")
    public String test1(){
//        // oshi的入口
//        SystemInfo systemInfo = new SystemInfo();
//        // 获取硬件
//        HardwareAbstractionLayer hardware = systemInfo.getHardware();
//        // 获取内存
//        GlobalMemory memory = hardware.getMemory();
        // 获取内存大小
        long total = memory.getTotal();
        // 获取可用内存大小
        long available = memory.getAvailable();
        double usagePercentage = (double) (total-available)/total;
        return "实际内存量："+total/1024/1024+"   可用内存大小:"+available/1024/1024+"   内存占用百分比："+usagePercentage*100;
    }

    @GetMapping("/test2")
    public String test2(){
        hmemory.record();
        List<GlobalMemory> history = hmemory.getHistory();
        return Arrays.toString(history.toArray());
    }

    @GetMapping("/test3")
    public String test3(){
        List<GlobalMemory> history = hmemory.getHistory();
        return Arrays.toString(history.toArray());
    }

    @GetMapping("/test4")
    public String test4(){
        double[] ticks = cpu.getProcessorCpuLoadBetweenTicks();
        double systemCpuLoad = cpu.getSystemCpuLoad();
        double cpuload2 = cpu.getSystemCpuLoadBetweenTicks();
        return cpuload2+"<br/>"+systemCpuLoad+"<br/>"+Arrays.toString(ticks);
    }

    @GetMapping("/test5")
    public String test5(){
        List<SnapshotCpu> history = hcpu.getHistory();
        return Arrays.toString(history.toArray());
    }

    @GetMapping("/test6")
    public String test6(){
        // 磁盘个数
        System.out.println(disks.size());
        HWDiskStore disk = disks.get(0);
        disk.updateDiskStats();
        // 在系统中磁盘设备的文件名
        System.out.println(disk.getName());
        // 硬盘总大小，单位byte
        System.out.println(disk.getSize());
        // 硬盘名称，一般显示厂商型号等
        System.out.println(disk.getModel());
        // 似乎是写入次数
        System.out.println(disk.getWrites());
        // 似乎是读取次数
        System.out.println(disk.getReads());
        // 序列号
        System.out.println(disk.getSerial());
        System.out.println("----------------");
        // 等待队列长
        System.out.println(disk.getCurrentQueueLength());
        // 分区表
        // System.ou|t.println(disk.getPartitions());
        // 读取总字节
        System.out.println(disk.getReadBytes());
        // 写入总字节
        System.out.println(disk.getWriteBytes());
        // 时间戳，与当前时间戳一般一样
        System.out.println(disk.getTimeStamp());
        System.out.println(System.currentTimeMillis());
        // 读写花费总时间数
        System.out.println(disk.getTransferTime());
        return "成功，详情看控制台打印";
    }
}
