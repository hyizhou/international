package xyz.yizhou.monitor.bean;

import oshi.hardware.HWDiskStore;

/**
 * 硬盘信息快照
 * @author huanggc
 * @date 2021/10/13 16:25
 */
public class SnapshotDisk {

    /** 磁盘总大小 */
    private long size;

    public static SnapshotDisk snapshot(HWDiskStore disk){
        SnapshotDisk snapshot = new SnapshotDisk();
        snapshot.size = disk.getSize();
        return snapshot;
    }
}
