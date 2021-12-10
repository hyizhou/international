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
     * 主页、上传页面
     */
    @GetMapping(value = {"/", "/index", "/index.html","/upload"})
    public String index() {
        return "index";
    }

    @GetMapping(value = "/aria2")
    public String aria2(){
        return "redirect:/aria2/index.html";
    }
}
