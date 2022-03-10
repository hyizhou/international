package top.hyizhou.monitor.control;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.hardware.CentralProcessor;
import top.hyizhou.monitor.bean.HistoryCpu;

/**
 * 提供cpu信息的接口
 *
 * @author hyizhou
 * @date 2021/10/15 15:56
 */
@RestController
@RequestMapping("/monitor/cpu")
public class ControlCpu {
    @Autowired
    private CentralProcessor cpu;
    @Autowired
    private HistoryCpu historyCpu;

    @GetMapping("/history")
    public String history(){
        return JSON.toJSONString(historyCpu.getHistory());
    }
    @GetMapping("/info")
    public String info(){
        return JSON.toJSONString(cpu);
    }
}
