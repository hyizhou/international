package top.hyizhou.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.hyizhou.framework.control.interceptor.AccessLimitInterceptor;

/**
 * springMVC配置
 * @author huanggc
 * @date 2021/11/9 11:32
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 过量请求拦截器
        registry.addInterceptor(new AccessLimitInterceptor());
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
