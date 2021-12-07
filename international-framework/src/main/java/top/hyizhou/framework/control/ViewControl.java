package top.hyizhou.framework.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 视图控制器
 * @author huanggc
 * @date 2021/12/7 16:13
 */
@Controller
public class ViewControl {
    /**
     * 主页
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
