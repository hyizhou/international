package top.hyizhou.framework.control.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 校验登录的拦截器
 * @author huanggc
 * @date 2021/12/23 10:49
 */
public class VerifyInterceptor implements HandlerInterceptor {
    private final Logger log = LoggerFactory.getLogger(VerifyInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("进入登录校验拦截器");
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
