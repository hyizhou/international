package xyz.yizhou.monitor.control;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.hardware.HWDiskStore;
import xyz.yizhou.monitor.bean.HistoryDisk;

import java.util.List;

/**
 * 提供硬盘信息的接口
 *
 * @author huanggc
 * @date 2021/10/15 16:05
 */
@RestController
@RequestMapping("/monitor/disk")
public class ControlDisk {

    private final List<HWDiskStore> diskList;
    private final HistoryDisk historyDisk;

    public ControlDisk(List<HWDiskStore> diskList, HistoryDisk historyDisk) {
        this.diskList = diskList;
        this.historyDisk = historyDisk;
    }

    @GetMapping("/history")
    public String history() {
        return JSON.toJSONString(historyDisk.getHistory());
    }

    @GetMapping("/info")
    public String info() {
        return JSON.toJSONString(diskList);
    }

}
