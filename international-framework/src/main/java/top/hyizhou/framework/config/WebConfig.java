package top.hyizhou.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.hyizhou.framework.config.property.BaseProperty;
import top.hyizhou.framework.control.interceptor.AccessLimitInterceptor;
import top.hyizhou.framework.control.interceptor.BadPathInterceptor;
import top.hyizhou.framework.control.interceptor.LogInterceptor;
import top.hyizhou.framework.control.interceptor.VerifyInterceptor;

/**
 * springMVC配置
 * @author hyizhou
 * @date 2021/11/9 11:32
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final VerifyInterceptor verifyInterceptor;
    @Autowired
    private BaseProperty property;

    public WebConfig(VerifyInterceptor verifyInterceptor) {
        this.verifyInterceptor = verifyInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 记录请求日志拦截器
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**");
        // 过量请求拦截器
        registry.addInterceptor(new AccessLimitInterceptor());
        // 验证登录拦截器
        if (!property.isSkipLogin()) {
            registry.addInterceptor(verifyInterceptor).excludePathPatterns("/test/**", "/register", "/error**");
        }
        // 公共仓库api请求路径检测拦截器
        registry.addInterceptor(new BadPathInterceptor()).addPathPatterns("/api/public/**");
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        WebMvcConfigurer.super.configurePathMatch(configurer);
    }
}
