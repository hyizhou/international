package xyz.yizhou.monitor.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import xyz.yizhou.monitor.bean.HistoryFileSystem;
import xyz.yizhou.monitor.bean.SnapshotFileStores;

import java.util.List;


/**
 * 文件系统信息接口
 *
 * @author huanggc
 * @date 2021/10/14 16:26
 */
@RestController
@RequestMapping("/monitor/file_system")
public class ControlFileSystem {
    @Autowired
    private FileSystem fileSystem;
    @Autowired
    private HistoryFileSystem historyFileSystem;

    @GetMapping("/history")
    public String history(){
        List<SnapshotFileStores> history = historyFileSystem.getHistory();
        StringBuilder a = new StringBuilder();
        for (SnapshotFileStores sna : history) {
            for (OSFileStore osFileStore : sna) {
                a.append("[").append(osFileStore.getMount()).append(",");
                a.append(osFileStore.getTotalSpace() / 1024 / 1024).append("M,");
                a.append(osFileStore.getUsableSpace() / 1024 / 1024).append("M]; ");
            }
            a.append("</br>");
        }
        return a.toString();
    }

    @GetMapping("/info")
    public String fileSystem(){
        return "";
    }

}
