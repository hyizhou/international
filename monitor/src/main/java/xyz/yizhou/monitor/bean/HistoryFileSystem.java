package xyz.yizhou.monitor.bean;

import oshi.software.os.FileSystem;
import xyz.yizhou.monitor.util.FixedQuery;

import java.util.List;

/**
 * 文件系统历史，记录磁盘各个分区的历史信息
 *
 * @author hyizhou
 * @date 2021/10/14 15:39
 */
public class HistoryFileSystem implements History<SnapshotFileStores>{
    private final FixedQuery<SnapshotFileStores> historyFileSystem = new FixedQuery<>(60);
    private final FileSystem fileSystem;
    public HistoryFileSystem(FileSystem fileSystem){
        this.fileSystem = fileSystem;
    }

    @Override
    public void record() {
        historyFileSystem.add(SnapshotFileStores.snapshot(this.fileSystem));
    }

    @Override
    public List<SnapshotFileStores> getHistory() {
        return this.historyFileSystem.toList();
    }
}
