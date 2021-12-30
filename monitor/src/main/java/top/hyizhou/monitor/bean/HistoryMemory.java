package top.hyizhou.monitor.bean;

import oshi.hardware.GlobalMemory;
import top.hyizhou.monitor.util.FixedQuery;

import java.util.List;

/**
 * 历史内存对象信息
 *
 * @author hyizhou
 * @date 2021/10/12 17:16
 */
public class HistoryMemory implements History<GlobalMemory> {
    /** 历史内存信息表 */
    private final FixedQuery<GlobalMemory> historyMemory = new FixedQuery<>(60);
    private final GlobalMemory realtimeMemory;

    public HistoryMemory(GlobalMemory memory){
        this.realtimeMemory = memory;
    }

    /**
     * 记录一次内存信息，向历史内存信息表中添加当前内存记录
     */
    @Override
    public void record(){
        historyMemory.add(SnapshotMemory.snapshot(realtimeMemory));
    }

    /**
     * 获取内存历史信息表数据
     * @return 历史内存信息表数据将以list返回
     */
    @Override
    public List<GlobalMemory> getHistory(){
        return historyMemory.toList();
    }

}
