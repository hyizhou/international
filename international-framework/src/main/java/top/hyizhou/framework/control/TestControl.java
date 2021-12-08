package top.hyizhou.framework.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import top.hyizhou.framework.utils.AccessLimit;

/**
 * @author huanggc
 * @date 2021/11/26 11:02
 */
@Controller
public class TestControl {
    @RequestMapping("/oo/{f:.*}")
    public String test1(@PathVariable String f){
        System.out.println(f);
        return "ok";
    }

    @RequestMapping("/test/2")
    @AccessLimit(seconds = 5, maxCount = 1)
    public String test2(){
        return "okk";
    }


    @RequestMapping("/test/3")
    public String test3(Model model){
        model.addAttribute("msg", "hello word");
        return "hello";
    }

    @RequestMapping("/test/4")
    public String test4(){
        return "hello";
    }
}
