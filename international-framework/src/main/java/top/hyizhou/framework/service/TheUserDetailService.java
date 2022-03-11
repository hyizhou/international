package top.hyizhou.framework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 用户提供给spring security进行身份认证
 * @author hyizhou
 * @date 2022/3/11 17:08
 */
@Service
public class TheUserDetailService implements UserDetailsService {
    private Logger logger = LoggerFactory.getLogger(TheUserDetailService.class);
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        String userName = "user3";
        String password = "1234";
        List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList("USER,");
        logger.info("收到认证：username={}", username);
        return new TheUsers(username, new BCryptPasswordEncoder().encode(password), authorityList);
    }

    // 直接继承已存在的UserDetails接口实现类
    public static final class TheUsers extends User {
        public TheUsers(String username, String password, Collection<? extends GrantedAuthority> authorities) {
            super(username, password, authorities);
        }

        public TheUsers(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
            super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        }
    }
}
