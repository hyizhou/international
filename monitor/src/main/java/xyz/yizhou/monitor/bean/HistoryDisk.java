package xyz.yizhou.monitor.bean;

import oshi.hardware.HWDiskStore;
import xyz.yizhou.monitor.util.FixedQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * 硬盘历史使用情况记录
 *
 * @author hyizhou
 * @date 2021/10/14 11:13
 */
public class HistoryDisk implements History<List<SnapshotDisk>>{
    private final FixedQuery<List<SnapshotDisk>> historyDisk = new FixedQuery<>(60);
    private final List<HWDiskStore> diskList;
    public HistoryDisk(List<HWDiskStore> diskList){
        this.diskList = diskList;
    }

    @Override
    public void record() {
        List<SnapshotDisk> snapshotDiskList = new ArrayList<>();
        for (HWDiskStore disk : diskList) {
            snapshotDiskList.add(SnapshotDisk.snapshot(disk));
        }
        this.historyDisk.add(snapshotDiskList);
    }

    @Override
    public List<List<SnapshotDisk>> getHistory() {
        return historyDisk.toList();
    }
}
