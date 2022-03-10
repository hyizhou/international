package top.hyizhou.monitor.bean;

import oshi.hardware.GlobalMemory;
import oshi.hardware.common.AbstractGlobalMemory;

/**
 * 内存快照对象，记录某一时刻的内存信息
 * @author hyizhou
 * @date 2021/10/12
 */
public class SnapshotMemory extends AbstractGlobalMemory {

    /**
     * 将保存调用本方法那一时刻的内存信息
     * @param memory 内存信息对象
     */
    public static GlobalMemory snapshot(GlobalMemory memory){
        SnapshotMemory snapshot = new SnapshotMemory();
        snapshot.memTotal = memory.getTotal();
        snapshot.memAvailable = memory.getAvailable();
        snapshot.swapTotal = memory.getSwapTotal();
        snapshot.swapUsed = memory.getSwapUsed();
        snapshot.swapPagesIn = memory.getSwapPagesIn();
        snapshot.swapPagesOut = memory.getSwapPagesOut();
        snapshot.pageSize = memory.getPageSize();
        return snapshot;
    }

    @Override
    protected void updateMeminfo() {

    }

    @Override
    protected void updateSwap() {

    }

    @Override
    public String toString() {
        return "MemorySnapshot{" +
                "memTotal=" + memTotal +
                ", memAvailable=" + memAvailable +
                ", swapTotal=" + swapTotal +
                ", swapUsed=" + swapUsed +
                ", swapPagesIn=" + swapPagesIn +
                ", swapPagesOut=" + swapPagesOut +
                ", pageSize=" + pageSize +
                '}';
    }
}
