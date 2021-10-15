package xyz.yizhou.monitor.bean;

import com.alibaba.fastjson.annotation.JSONField;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 一个文件分区的快照
 *
 * @author huanggc
 * @date 2021/10/14 15:29
 */
public class SnapshotFileStores implements Iterable<OSFileStore> {
    /** 记录分区情况的对象 */
    private List<OSFileStore> osFileStores;

    public static SnapshotFileStores snapshot(FileSystem fileSystem){
        // 得到最新的分区信息对象
        OSFileStore[] fileStores = fileSystem.getFileStores();
        SnapshotFileStores snapshot = new SnapshotFileStores();
        snapshot.osFileStores = Arrays.asList(fileStores);
        return snapshot;
    }

    public List<OSFileStore> getOsFileStores() {
        return osFileStores;
    }

    /**
     * 各分区名称
     */
    @JSONField(serialize = false)
    public List<String> getPartitionName(){
        List<String> names = new ArrayList<>();
        for (OSFileStore osFileStore : osFileStores) {
            names.add(osFileStore.getName());
        }
        return names;
    }

    @Override
    public String toString() {
        return "SnapshotFileStore{" +
                "osFileStore=" + Arrays.toString(getPartitionName().toArray()) +
                '}';
    }

    @Override
    public Iterator<OSFileStore> iterator() {
        return osFileStores.iterator();
    }
}
