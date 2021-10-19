package xyz.yizhou.monitor.bean;

import oshi.hardware.NetworkIF;
import xyz.yizhou.monitor.util.FixedQuery;

import java.util.List;

/**
 * 网速历史记录
 * @author huanggc
 * @date 2021/10/18 17:33
 */
public class HistoryNet implements History<SnapshotNet>{
    private final List<NetworkIF> netList;
    private final FixedQuery<SnapshotNet> historyNet = new FixedQuery<>(60);
    public HistoryNet(List<NetworkIF> netList){
        this.netList = netList;
    }

    @Override
    public void record() {
        SnapshotNet snapshot = SnapshotNet.snapshot(netList);
        historyNet.add(snapshot);
    }

    @Override
    public List<SnapshotNet> getHistory() {
        return historyNet.toList();
    }
}
