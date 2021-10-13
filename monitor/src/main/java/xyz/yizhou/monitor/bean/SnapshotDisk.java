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
    /** 使用空间大小 */
    private long useSize;
    /** 读取速度，单位byte/s */
    private double readSpeed;
    /** 写入速度，单位byte/s */
    private double writeSpeed;

    /**
     * 由自动任务调用，一秒钟调用一次
     * @param disk 硬盘对象，代表一块硬盘
     * @return 本类的实例
     */
    public static SnapshotDisk snapshot(HWDiskStore disk){
        long lastReadSize = disk.getReadBytes();
        long lastWriteSize = disk.getWriteBytes();
        // 手动刷新信息
        disk.updateDiskStats();
        SnapshotDisk snapshot = new SnapshotDisk();
        snapshot.size = disk.getSize();
        long readSize = disk.getReadBytes();
        long writeSize = disk.getWriteBytes();
        snapshot.readSpeed = (double) (readSize - lastReadSize);
        snapshot.writeSpeed = (double) (writeSize - lastWriteSize);
        return snapshot;
    }
}
