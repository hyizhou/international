package top.hyizhou.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.hyizhou.framework.control.interceptor.AccessLimitInterceptor;
import top.hyizhou.framework.control.interceptor.LogInterceptor;
import top.hyizhou.framework.control.interceptor.VerifyInterceptor;

/**
 * springMVC配置
 * @author huanggc
 * @date 2021/11/9 11:32
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 记录请求日志拦截器
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**");
        // 过量请求拦截器
        registry.addInterceptor(new AccessLimitInterceptor());
        // 验证登录拦截器
        registry.addInterceptor(new VerifyInterceptor());
    }
}
