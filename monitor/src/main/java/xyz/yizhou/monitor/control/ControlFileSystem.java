package xyz.yizhou.monitor.control;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import xyz.yizhou.monitor.bean.HistoryFileSystem;

import java.util.ArrayList;
import java.util.List;


/**
 * 文件系统信息接口
 *
 * @author huanggc
 * @date 2021/10/14 16:26
 */
@RestController
@RequestMapping("/monitor/filesystem")
public class ControlFileSystem {
    private final FileSystem fileSystem;
    private final HistoryFileSystem historyFileSystem;

    public ControlFileSystem(FileSystem fileSystem, HistoryFileSystem historyFileSystem) {
        this.fileSystem = fileSystem;
        this.historyFileSystem = historyFileSystem;
    }

    /**
     * 文件系统历史记录
     */
    @GetMapping("/history")
    public String history(){
        return JSON.toJSONString(historyFileSystem);
    }

    /**
     * 获得文件系统当前分区的所有信息
     */
    @GetMapping("/info")
    public String fileSystem(){
        return JSON.toJSONString(fileSystem.getFileStores());
    }

    /**
     * 得到主要信息，也就是分区容量情况
     */
    @GetMapping("/major")
    public String major(){
        class Major {
            public String mount;
            public long totalSpace;
            public long usableSpace;
        }
        List<Major> reply = new ArrayList<>();
        OSFileStore[] fileStores = fileSystem.getFileStores();
        for (OSFileStore fileStore : fileStores) {
            Major nn = new Major();
            nn.mount = fileStore.getMount();
            nn.usableSpace = fileStore.getUsableSpace();
            nn.totalSpace = fileStore.getTotalSpace();
            reply.add(nn);
        }
        return JSON.toJSONString(reply);
    }

}
