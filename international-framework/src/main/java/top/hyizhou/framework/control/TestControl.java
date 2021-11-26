package top.hyizhou.framework.control;

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
    @RequestMapping("/1")
    public String test1(){
        return "ok";
    }

    @RequestMapping("/2")
    @AccessLimit(seconds = 5, maxCount = 1)
    public String test2(){
        return "okk";
    }
}
