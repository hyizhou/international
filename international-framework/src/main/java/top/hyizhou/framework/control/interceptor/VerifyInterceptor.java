package top.hyizhou.framework.control.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.hyizhou.framework.config.constant.CookieConstant;
import top.hyizhou.framework.utils.CookieUtil;
import top.hyizhou.framework.utils.container.LoggedOnContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 校验登录的拦截器
 * 已经登录：全部放行，登录页跳转到主页
 * 未登录：除注册，任何请求都重定向到登录页面
 * @author hyizhou
 * @date 2021/12/23 10:49
 */
@Component
public class VerifyInterceptor implements HandlerInterceptor {
    private final Logger log = LoggerFactory.getLogger(VerifyInterceptor.class);
    /** 已登录用户容器 */
    private final LoggedOnContainer container;

    public VerifyInterceptor(LoggedOnContainer container) {
        this.container = container;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean isLoginUrl =  "/login".equals(request.getRequestURI());
        // 通过cookie判断是否登录
        Map<String, Cookie> cookieMap = CookieUtil.getCookieMap(request);
        Cookie signInCookie = cookieMap.get(CookieConstant.SIGN_IN);
        // 未登录状态
        if (signInCookie == null || !judge(signInCookie.getValue())){
            // 未登录访问登录页，放行；其他页面跳转到登录页
            if (isLoginUrl){
                log.debug(">>> 登录校验拦截器--进入登录页");
                return true;
            }
            log.debug(">>> 登录校验拦截器--验证失败，跳转到登录页");
            response.sendRedirect("/login");
            return false;
        }
        // 已登录状态
        // 刷新登录状态cookie存活时间
        signInCookie.setMaxAge(CookieConstant.SIGN_MAX_AGE);
        signInCookie.setPath("/");
        response.addCookie(signInCookie);
        // 刷新容器中登录信息存活时间
        container.updateSurvivalTime(signInCookie.getValue(), CookieConstant.SIGN_MAX_AGE);
        // 已登录且访问登录页，则跳转到主页
        if (isLoginUrl){
            log.debug(">>> 登录校验拦截器--已登录跳转主页");
            response.sendRedirect("/index");
        }
        log.debug(">>> 登录校验拦截器--已验证登录");
        return true;
    }

    /**
     * 判断cookie中登录标识符内容是否正确 <br/>
     * 标识符为登录标识，判断标志是否在已登录用户列表中
     * @param value 标识符内容
     * @return true则是正确的
     */
    private boolean judge(String value){
        log.debug("登录校验码：{}", value);
        if (value == null){
            return false;
        }
        return container.exist(value);
    }
}
