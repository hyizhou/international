package top.hyizhou.framework.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author huanggc
 * @date 2021/11/26 11:02
 */
@Controller
public class TestControl {
    @RequestMapping("/test/1")
    public String test1(RedirectAttributes attributes){
        System.out.println("到达test1");
//        attributes.addAttribute("aa", "这里是aa参数");
        attributes.addFlashAttribute("aa", "这是啊啊");
        return "redirect:/test/2";
    }

    @RequestMapping("/test/2")
    @ResponseBody
    public String test2(@ModelAttribute("aa") String aa){
        System.out.println("到达test2");
        System.out.println("输出aa："+ aa);
        return "ok";
    }
}
