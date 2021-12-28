package top.hyizhou.framework.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import top.hyizhou.framework.config.constant.CookieConstant;
import top.hyizhou.framework.service.LoginService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录功能控制器
 *
 * @author hyizhou
 * @date 2021/12/23 9:57
 */
@RequestMapping()
@Controller
public class LoginControl {

    private final Logger log = LoggerFactory.getLogger(LoginControl.class);
    private final LoginService loginService;

    public LoginControl(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 处理登录验证信息
     * @param userName 用户名
     * @param password 密码
     * @return 若验证未通过，则返回login页面，且页面具有相关错误提示；若验证通过，则跳转到主页
     */
    @PostMapping("/login")
    public String login(@ModelAttribute("username") String userName, @ModelAttribute("password") String password,
                        HttpServletResponse response, Model model) {
        log.info("接收到请求-username={};password={}", userName, password);
        if (loginService.allowLogin(userName, password)) {
            Cookie cookie = new Cookie(CookieConstant.SIGN_IN, userName);
            cookie.setMaxAge(CookieConstant.SIGN_MAX_AGE);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:/index";
        }
        log.info("密码错误");
        model.addAttribute("alert", "密码或账户名错误");
        return "login/login";
    }

    /**
     * 响应登录视图
     */
    @GetMapping("/login")
    public String login() {
        log.info("响应登录视图");
        return "login/login";
    }
}
