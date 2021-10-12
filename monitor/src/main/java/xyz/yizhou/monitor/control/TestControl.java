package xyz.yizhou.monitor.control;

import com.fasterxml.jackson.annotation.JsonGetter;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import xyz.yizhou.monitor.bean.HistoryMemory;

import java.util.Arrays;
import java.util.List;

/**
 * 用于测试的控制类
 * @author huanggc
 * @date 2021/10/12 15:18
 */
@RestController
@RequestMapping
public class TestControl {
    @Autowired
    private GlobalMemory memory;
    @Autowired
    private HistoryMemory hmemory;

    @GetMapping("/test")
    public String test(){
        return "<h1>Test</h1>";
    }

    @GetMapping("/test1")
    public String test1(){
//        // oshi的入口
//        SystemInfo systemInfo = new SystemInfo();
//        // 获取硬件
//        HardwareAbstractionLayer hardware = systemInfo.getHardware();
//        // 获取内存
//        GlobalMemory memory = hardware.getMemory();
        // 获取内存大小
        long total = memory.getTotal();
        // 获取可用内存大小
        long available = memory.getAvailable();
        double usagePercentage = (double) (total-available)/total;
        return "实际内存量："+total/1024/1024+"   可用内存大小:"+available/1024/1024+"   内存占用百分比："+usagePercentage*100;
    }

    @GetMapping("/test2")
    public String test2(){
        hmemory.record();
        List<GlobalMemory> history = hmemory.getHistory();
        return Arrays.toString(history.toArray());
    }
}
