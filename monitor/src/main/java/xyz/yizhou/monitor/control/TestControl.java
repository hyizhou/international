package xyz.yizhou.monitor.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import xyz.yizhou.monitor.bean.*;

import java.io.File;
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
    @Autowired
    private HistoryDisk historyDisk;
    @Autowired
    private FileSystem fileSystem;
    @Autowired
    private HistoryFileSystem historyFileSystem;

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
        System.out.println("硬盘可用空间："+File.listRoots()[0].getFreeSpace());
        System.out.println("盘符："+File.listRoots()[0].getAbsolutePath());

        return "成功，详情看控制台打印";
    }

    @GetMapping("/test7")
    public String test7(){
        OSFileStore[] osFileStores = fileSystem.getFileStores();
        int size = osFileStores.length;
        System.out.println("盘符个数："+size);
        for (OSFileStore osFileStore : osFileStores) {
            System.out.println("类型："+osFileStore.getType());
            System.out.println("名称："+osFileStore.getName());
            System.out.println("分区空间："+osFileStore.getTotalSpace());
            System.out.println("可用空间"+osFileStore.getUsableSpace());
            System.out.println("逻辑符："+osFileStore.getMount());
            System.out.println("逻辑卷，仅在linux上有："+osFileStore.getLogicalVolume());
            System.out.println(osFileStore.getDescription());
            System.out.println("------------------");
        }
        return "查看控制台";
    }

    @GetMapping("/test8")
    public String test8(){
        StringBuilder msg = new StringBuilder();
        List<List<SnapshotDisk>> history = historyDisk.getHistory();
        for (List<SnapshotDisk> snapshotDiskList : history) {
            for (SnapshotDisk snapshotDisk : snapshotDiskList) {
                msg.append(snapshotDisk.toString()).append(";");
            }
            msg.append("</br>");
        }
        return msg.toString();
    }

}
