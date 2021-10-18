package xyz.yizhou.monitor.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import oshi.hardware.NetworkIF;
import xyz.yizhou.monitor.bean.HistoryCpu;
import xyz.yizhou.monitor.bean.HistoryDisk;
import xyz.yizhou.monitor.bean.HistoryFileSystem;
import xyz.yizhou.monitor.bean.HistoryMemory;

import java.util.Arrays;
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
    private HistoryDisk hDisk;
    @Autowired
    private HistoryFileSystem hFileSystem;
    @Autowired
    private List<NetworkIF> networkIFList;
    /**
     * 每秒记录内存信息定时任务
     */
    @Scheduled(cron = "0/1 * * * * *")
    public void taskMemory(){
        historyMemory.record();
    }

    /**
     * 每秒记录cpu使用情况
     */
    @Scheduled(cron = "0/1 * * * * *")
    public void taskCpu(){
        historyCpu.record();
    }

    /**
     * 每秒记录硬盘传输速度
     */
    @Scheduled(cron = "0/1 * * * * *")
    public void taskDisk(){
        hDisk.record();
    }

    /**
     * 每秒记录文件系统使用情况
     */
    @Scheduled(cron = "0/1 * * * * *")
    public void taskFileSystem(){
        hFileSystem.record();
    }

    @Scheduled(cron = "0/1 * * * * *")
    public void taskTest(){
        for (NetworkIF networkIF : networkIFList) {
            long sent = networkIF.getBytesSent();
            long recv = networkIF.getBytesRecv();
            networkIF.updateNetworkStats();
            System.out.println("----------------");
            System.out.println(Arrays.toString(networkIF.getIPv4addr()));
            System.out.println(networkIF.getBytesRecv());
            System.out.println(networkIF.getBytesSent());
            System.out.println(networkIF.getSpeed()+"网口速度");
            System.out.println("速度：" + (double)(networkIF.getBytesSent()-sent)/1024+"kb/s");
        }
    }
}
