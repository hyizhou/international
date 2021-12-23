package top.hyizhou.framework.control.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 记录请求相关日志信息的拦截器
 * @author huanggc
 * @date 2021/12/23 14:48
 */
public class LogInterceptor implements HandlerInterceptor {
    private final Logger log = LoggerFactory.getLogger(LogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("新的请求--请求地址：{}, 客户机ip：{}", request.getRequestURI(), request.getRemoteAddr());
        return true;
    }
}
