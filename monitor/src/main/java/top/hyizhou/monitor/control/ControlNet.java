package top.hyizhou.monitor.control;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.hardware.NetworkIF;
import oshi.software.os.NetworkParams;
import top.hyizhou.monitor.bean.HistoryNet;

import java.util.List;

/**
 * 网络信息提供接口
 * @author hyizhou
 * @date 2021/10/19 9:55
 */
@RestController
@RequestMapping("/monitor/net")
public class ControlNet {
    private final List<NetworkIF> netList;
    private final HistoryNet history;
    private final NetworkParams networkParams;

    public ControlNet(List<NetworkIF> netList, HistoryNet history, NetworkParams networkParams) {
        this.netList = netList;
        this.history = history;
        this.networkParams = networkParams;
    }


    @GetMapping("/info")
    public String info(){
        return JSON.toJSONString(netList);
    }

    @GetMapping("/history")
    public String history(){
        return JSON.toJSONString(history.getHistory());
    }

    /**
     * 得到系统dns信息
     */
    @GetMapping("/dns")
    public String dns(){
        return JSON.toJSONString(networkParams.getDnsServers());
    }
}
