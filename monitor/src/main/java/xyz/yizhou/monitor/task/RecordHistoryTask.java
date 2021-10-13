package xyz.yizhou.monitor.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import oshi.hardware.HWDiskStore;
import xyz.yizhou.monitor.bean.HistoryCpu;
import xyz.yizhou.monitor.bean.HistoryMemory;

import java.util.List;

/**
 * 记录历史信息定时任务
 *
 * @author huanggc
 * @date 2021/10/13 10:15
 */
@Component
public class RecordHistoryTask {
    @Autowired
    private HistoryMemory historyMemory;
    @Autowired
    private HistoryCpu historyCpu;
    @Autowired
    private List<HWDiskStore> disks;

    /**
     * 每秒记录内存信息定时任务
     */
    @Scheduled(cron = "0/1 * * * * *")
    public void taskMemory(){
        historyMemory.record();
    }

    @Scheduled(cron = "0/1 * * * * *")
    public void taskCpu(){
        historyCpu.record();
    }

    @Scheduled(cron = "0/1 * * * * *")
    public void taskDisk() throws InterruptedException {
        HWDiskStore disk = disks.get(0);
        long readBytes1 = disk.getReadBytes();
        System.out.println("上一次readBytes值："+ readBytes1);
        Thread.sleep(1000);
        disk.updateDiskStats();
        long readBytes2 = disk.getReadBytes();
        System.out.println("readBytes值："+readBytes2);
        System.out.println("当前读取速度："+(double)(readBytes2-readBytes1)/1024/1024);
    }
}
