package top.hyizhou.framework.control;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hyizhou.framework.utils.AccessLimit;

/**
 * @author huanggc
 * @date 2021/11/26 11:02
 */
@RequestMapping("/test")
@RestController
public class TestControl {
    @RequestMapping("/oo/{f:.*}")
    public String test1(@PathVariable String f){
        System.out.println(f);
        return "ok";
    }

    @RequestMapping("/2")
    @AccessLimit(seconds = 5, maxCount = 1)
    public String test2(){
        return "okk";
    }
}
