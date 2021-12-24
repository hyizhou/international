package xyz.yizhou.monitor.bean;

import oshi.hardware.CentralProcessor;

import java.util.Arrays;

/**
 * cpu使用率快照 </br>
 *
 * @author hyizhou
 * @date 2021/10/13 10:34
 */
public class SnapshotCpu {
    /** cpu负载 */
    private double cpuLoad;
    /** 每个核的负载 */
    private double[] cpuLoadCores;

    public static SnapshotCpu snapshot(CentralProcessor cpu){
        SnapshotCpu snapshot = new SnapshotCpu();
        // 最近cpu使用情况，若不可用为负值
        snapshot.cpuLoad = cpu.getSystemCpuLoad();
        // 记录每个核的使用率，更新间隔为1s
        snapshot.cpuLoadCores = cpu.getProcessorCpuLoadBetweenTicks();
        return snapshot;
    }

    public double getCpuLoad(){
        return this.cpuLoad;
    }

    public double[] getCpuLoadCores(){
        return this.cpuLoadCores;
    }

    @Override
    public String toString() {
        return "CpuSnapshot{" +
                "cpuLoad=" + cpuLoad +
                ", cpuLoadCores=" + Arrays.toString(cpuLoadCores) +
                '}';
    }
}
