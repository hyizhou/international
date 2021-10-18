package xyz.yizhou.monitor.bean;

import oshi.hardware.NetworkIF;

import java.util.List;

/**
 * 网络快照，记录当前网速
 * @author huanggc
 * @date 2021/10/18 17:25
 */
public class SnapshotNet {
    /** 发送速度，单位byte/s */
    private long sendSpeed = 0 ;
    /** 接收速度 */
    private long recvSpeed = 0;

    public static SnapshotNet snapshot(List<NetworkIF> nets){
        SnapshotNet snap = new SnapshotNet();
        // 所有网卡总发送速度
        for (NetworkIF net : nets) {
            long send = net.getBytesSent();
            long recv = net.getBytesRecv();
            net.updateNetworkStats();
            snap.recvSpeed += net.getBytesRecv() - recv;
            snap.sendSpeed += net.getBytesSent() -send;
        }

        return snap;
    }

    public long getSendSpeed() {
        return sendSpeed;
    }

    public long getRecvSpeed() {
        return recvSpeed;
    }

    @Override
    public String toString() {
        return "SnapshotNet{" +
                "sendSpeed=" + sendSpeed +
                ", recvSpeed=" + recvSpeed +
                '}';
    }
}
