package top.hyizhou.monitor.bean;

import oshi.hardware.CentralProcessor;
import top.hyizhou.monitor.util.FixedQuery;

import java.util.List;

/**
 * @author hyizhou
 * @date 2021/10/13 11:48
 */
public class HistoryCpu implements History<SnapshotCpu>{
    private final FixedQuery<SnapshotCpu> historyCpu = new FixedQuery<>(60);
    private final CentralProcessor cpu;
    public HistoryCpu(CentralProcessor cpu){
        this.cpu = cpu;
    }

    @Override
    public void record() {
        historyCpu.add(SnapshotCpu.snapshot(cpu));
    }

    @Override
    public List<SnapshotCpu> getHistory() {
        return historyCpu.toList();
    }
}
