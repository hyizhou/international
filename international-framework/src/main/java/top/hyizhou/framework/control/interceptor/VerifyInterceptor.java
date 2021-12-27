package top.hyizhou.framework.control.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import top.hyizhou.framework.config.constant.CookieConstant;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 校验登录的拦截器
 * 已经登录：全部放行，登录页跳转到主页
 * 未登录：除注册，任何请求都重定向到登录页面
 * @author hyizhou
 * @date 2021/12/23 10:49
 */
public class VerifyInterceptor implements HandlerInterceptor {
    private final Logger log = LoggerFactory.getLogger(VerifyInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("进入登录校验拦截器");
        boolean passed = false;
        // 通过cookie判断是否登录
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(CookieConstant.SIGN_IN)) {
                    passed = judge(cookie.getValue());
                }
            }
        }
        if (!passed){
            log.info("cookie值显示未登录哦");
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }

    /**
     * 判断cookie中登录标识符内容是否正确
     * @param value 标识符内容
     * @return true则是正确的
     */
    private boolean judge(String value){
        if (value == null){
            return false;
        }
        return true;
    }
}