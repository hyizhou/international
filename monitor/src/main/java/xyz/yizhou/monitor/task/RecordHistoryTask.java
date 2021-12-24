package xyz.yizhou.monitor.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.yizhou.monitor.bean.*;

/**
 * 记录历史信息定时任务
 *
 * @author hyizhou
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
    private HistoryNet historyNet;

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
    public void taskNet(){
        historyNet.record();
    }

}
