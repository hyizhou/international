package top.hyizhou.monitor.control;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.software.os.OperatingSystem;

/**
 * 操作系统信息接口
 * @author hyizhou
 * @date 2021/10/19 11:44
 */
@RestController
@RequestMapping("/monitor/os")
public class ControlOs {
    @Autowired
    private OperatingSystem os;

    @GetMapping("/info")
    public String info(){
        return JSON.toJSONString(os);
    }
}
