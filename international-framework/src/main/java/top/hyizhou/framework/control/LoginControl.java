package top.hyizhou.framework.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 登录功能控制器
 * @author huanggc
 * @date 2021/12/23 9:57
 */
@RequestMapping()
@Controller
public class LoginControl {
    @PostMapping("/login")
    public String login(String userName, String password){
        return null;
    }
}
