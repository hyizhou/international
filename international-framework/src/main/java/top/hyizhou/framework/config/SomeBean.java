package top.hyizhou.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import top.hyizhou.framework.utils.container.LoggedOnContainer;

/**
 * 产生一些Bean
 * @author hyizhou
 * @date 2021/11/9 15:29
 */
@Configuration
public class SomeBean {
    /**
     * spring提供的ant路径匹配器
     */
    @Bean
    public AntPathMatcher antPathMatcher(){
        return new AntPathMatcher();
    }

    /**
     * 已登录状态存储容器
     */
    @Bean
    public LoggedOnContainer loggedOnContainer(){
        return new LoggedOnContainer();
    }

}
