package top.hyizhou.framework.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import top.hyizhou.framework.entity.Resp;

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

    /**
     * aira2控制界面
     */
    @GetMapping(value = "/aria2")
    public String aria2(){
        return "redirect:/aria2/index.html";
    }

    @GetMapping(value = "/dirs/{id}/**")
    public ModelAndView dir(@PathVariable("id") int id){
        if (id == 1){
            ModelAndView dir = new ModelAndView("dir");
            dir.addObject("dirName", "workspace");
            dir.addObject("dirPath", "1/workspace");
            return dir;
        }
        Resp<String> stringResp = new Resp<String>("100", "ok", "aa");
        ModelAndView json = new ModelAndView(new MappingJackson2JsonView());
        json.addObject(stringResp);
        json.addObject("view", "json");
        return json;
    }
}
