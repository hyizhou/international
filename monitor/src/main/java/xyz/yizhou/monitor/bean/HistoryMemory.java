package xyz.yizhou.monitor.bean;

import cn.hutool.core.bean.BeanUtil;
import jdk.nashorn.internal.objects.Global;
import oshi.hardware.GlobalMemory;
import xyz.yizhou.monitor.util.FixedQuery;

import java.util.List;

/**
 * 历史内存对象信息
 *
 * @author huanggc
 * @date 2021/10/12 17:16
 */
public class HistoryMemory {
    /** 历史内存信息表 */
    private final FixedQuery<GlobalMemory> historyMemory = new FixedQuery<>(6);
    private final GlobalMemory realtimeMemory;

    public HistoryMemory(GlobalMemory memory){
        this.realtimeMemory = memory;
    }

    /**
     * 向历史内存信息表中添加当前内存记录
     */
    public void record(){
        historyMemory.add(MemorySnapshot.snapshot(realtimeMemory));
    }

    /**
     * 获取内存历史信息表数据
     * @return 历史内存信息表数据将以list返回
     */
    public List<GlobalMemory> getHistory(){
        return historyMemory.toList();
    }

}
