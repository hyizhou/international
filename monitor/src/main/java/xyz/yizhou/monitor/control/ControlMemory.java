package xyz.yizhou.monitor.control;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.hardware.GlobalMemory;
import xyz.yizhou.monitor.bean.HistoryMemory;

/**
 * 内存信息提供的接口
 *
 * @author hyizhou
 * @date 2021/10/15 15:45
 */
@RestController
@RequestMapping("/monitor/memory")
public class ControlMemory {
    private final HistoryMemory historyMemory;
    private final GlobalMemory globalMemory;

    public ControlMemory(HistoryMemory historyMemory, GlobalMemory globalMemory) {
        this.historyMemory = historyMemory;
        this.globalMemory = globalMemory;
    }

    /**
     * 内存历史状况
     */
    @GetMapping("/history")
    public String history(){
        return JSON.toJSONString(historyMemory.getHistory());
    }

    /**
     * 内存当前状况
     */
    @GetMapping("/info")
    public String info(){
        return JSON.toJSONString(globalMemory);
    }
}
