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
import top.hyizhou.framework.entity.User;
import top.hyizhou.framework.service.AccountService;
import top.hyizhou.framework.utils.CookieUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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
    private final AccountService accountService;

    public LoginControl(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 处理登录验证信息
     * @param accountName 用户名
     * @param password 密码
     * @return 若验证未通过，则返回login页面，且页面具有相关错误提示；若验证通过，则跳转到主页
     */
    @PostMapping("/login")
    public String login(@ModelAttribute("username") String accountName, @ModelAttribute("password") String password,
                        HttpServletResponse response, Model model) {
        log.info("接收到请求-username={};password={}", accountName, password);
        User user = accountService.verify(accountName, password);
        if (user != null) {
            Cookie cookie = new Cookie(CookieConstant.SIGN_IN, accountName);
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
     * 注册接口
     * TODO 接口参数改成bean比较好
     * @return 成功则返回登录页面，并提示注册成功。失败则继续处于注册页面，并提示错误原因
     */
    @PostMapping(value = "/register")
    public String register(@ModelAttribute("userName") String accountName, @ModelAttribute("password") String password, Model model){
        // 前端页面若使用json能直接包装成实体类
        User user = new User();
        user.setAccountName(accountName);
        user.setPassword(password);
        user.setName("注册用户");
        String errMsg = accountService.register(user);
        if (errMsg == null) {
            // 注册成功
            log.info("用户注册成功：id: {}---accountName: {}", user.getId(), user.getAccountName());
            model.addAttribute("alert", "注册成功，请继续登录");
            return "login/login";
        }else {
            // 注册失败
            log.info("用户注册失败，accountName=[{}]; 提示信息：{}", user.getAccountName(), errMsg);
            model.addAttribute("msg", "注册失败，请检查参数输入情况");
            return "login/register";
        }
    }

    /**
     * 注销，原理是将页面cookie生命设置为0，导致其删除，后端部分并没有做什么事情，其实删除cookie事情前端js也能做
     * @return 跳转到登录页
     */
    @GetMapping("/logout")
    public String logout( HttpServletRequest req, HttpServletResponse resp){
        Map<String, Cookie> cookieMap = CookieUtil.getCookieMap(req);
        Cookie cookie = cookieMap.get(CookieConstant.SIGN_IN);
        if (cookie != null){
            log.info("{}用户进行注销操作",cookie.getValue());
            cookie.setMaxAge(0);
            cookie.setPath("/");
            resp.addCookie(cookie);
        }
        return "redirect:/login/login";
    }
}
