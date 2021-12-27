package top.hyizhou.framework.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import top.hyizhou.framework.config.constant.CookieConstant;
import top.hyizhou.framework.service.LoginService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
     *
     * @param userName 用户名
     * @param password 密码
     */
    @PostMapping("/login")
    public String login(@ModelAttribute("username") String userName, @ModelAttribute("password") String password,
                        HttpServletResponse response, HttpServletRequest request) {
        log.info("接收到请求-username={};password={}", userName, password);
        if (loginService.allowLogin(userName, password)) {
            Cookie cookie = new Cookie(CookieConstant.SIGN_IN, userName);
            cookie.setMaxAge(30 * 60);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:/index";
        }
        log.info("密码错误");
        return "redirect:/login";
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
