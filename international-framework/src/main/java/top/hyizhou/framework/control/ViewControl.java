package top.hyizhou.framework.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 简单视图的控制器
 * @author huanggc
 * @date 2021/12/7 16:13
 */
@Controller
public class ViewControl {
    /**
     * 上传页面
     */
    @GetMapping(value = {"/upload"})
    public String index() {
        return "upload/upload";
    }

    /**
     * aira2控制界面
     */
    @GetMapping(value = "/aria2")
    public String aria2(){
        return "redirect:/aria2/index.html";
    }

    @GetMapping(value = {"/", "/index"})
    public String mainView(){
        return "index";
    }

}
