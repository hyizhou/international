package top.hyizhou.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import top.hyizhou.framework.service.TheUserDetailService;

/**
 * @author hyizhou
 * @date 2022/3/10 17:01
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private TheUserDetailService userDetailService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/index","/").authenticated();

        http.formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/")
                .permitAll(true)
                .failureUrl("/login?nonono");

        http.logout()
                .permitAll()
                // 注销地址
                .logoutUrl("/logout")
                // 注销请求匹配器，用于指定注销地址和请求方式
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                // 注销成功后跳转地址
                .logoutSuccessUrl("/login")
                // 注销成功后要删除的cookie
                .deleteCookies()
                // 清除认证信息，默认true
                .clearAuthentication(true)
                // 使session失效
                .invalidateHttpSession(true);


        http.csrf().disable();
    }
}
