package top.hyizhou.framework.control;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import top.hyizhou.framework.utils.AccessLimit;
import top.hyizhou.framework.utils.ValueCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器，控制请求密度
 * @author huanggc
 * @date 2021/11/25 10:58
 */
public class AccessLimitInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (null == accessLimit){
                return true;
            }
            int maxCount = accessLimit.maxCount();
            if (maxCount < 0){
                return true;
            }
            int seconds = accessLimit.seconds();
            boolean needLogin = accessLimit.needLogin();
            if (needLogin){
                // TODO 判断是否登录
            }
            String ip = request.getRemoteAddr();
            String key = request.getServletPath() + ":" + ip;
            Integer count = (Integer) ValueCache.get(key);
            if (count == null){
                ValueCache.set(key, 1, seconds);
                return true;
            }
            if (count < maxCount){
                count += 1;
                // 变量有可能get时没过期，到这update时过期，产生空指针异常
                try {
                    ValueCache.update(key, count);
                }catch (NullPointerException e){
                    // 可看前面代码在上一个周期，这里在下一个周期，整个请求算新的周期
                    ValueCache.set(key, 1, seconds);
                }
                return true;
            }
            throw new RuntimeException("请求过于频繁");
        }
        return true;
//        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}
