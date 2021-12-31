package top.hyizhou.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import top.hyizhou.framework.mapper.UserMapping;
import top.hyizhou.framework.proxy.UserMappingProxy;

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
     * 数据库查询代理类
     */
    @Bean
    public UserMapping userMapping(){
        return new UserMappingProxy();
    }
}
