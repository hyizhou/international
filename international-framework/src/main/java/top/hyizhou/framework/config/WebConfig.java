package top.hyizhou.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.hyizhou.framework.control.interceptor.AccessLimitInterceptor;
import top.hyizhou.framework.control.interceptor.BadPathInterceptor;
import top.hyizhou.framework.control.interceptor.LogInterceptor;

/**
 * springMVC配置
 * @author hyizhou
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
        // 公共仓库api请求路径检测拦截器
        registry.addInterceptor(new BadPathInterceptor()).addPathPatterns("/api/public/**");
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        WebMvcConfigurer.super.configurePathMatch(configurer);
    }
}
