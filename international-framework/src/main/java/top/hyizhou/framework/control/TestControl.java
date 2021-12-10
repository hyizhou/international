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
        int[] list = {1,2,3,3,4,5};
        model.addAttribute("msg", "hello word");
        model.addAttribute("oo", 4);
        model.addAttribute("class", new A());
        model.addAttribute("list", list);
        return "hello";
    }

    @RequestMapping("/test/4")
    public String test4(){
        return "hello";
    }

    public class A{
        private String a;
        private String b;
        private String c;

        public A(){
            a = "1";
            b = "b";
            c = "c";
        }

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }
    }
}
