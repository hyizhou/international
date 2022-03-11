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
import top.hyizhou.framework.mapper.UsersMapper;

import java.util.List;

/**
 * 用户提供给spring security进行身份认证
 * @author hyizhou
 * @date 2022/3/11 17:08
 */
@Service
public class TheUserDetailServiceImpl implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(TheUserDetailServiceImpl.class);
    private final UsersMapper usersMapper;

    public TheUserDetailServiceImpl(UsersMapper usersMapper) {
        this.usersMapper = usersMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        top.hyizhou.framework.entity.User user = usersMapper.findByAccountName(username);
        if (user == null){
            logger.info("没有此用户："+username);
            throw new UsernameNotFoundException("未找到此用户："+username);
        }
        String password = user.getPassword();
        List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList("USER,");
        logger.info("收到认证：username={}", username);
        return new User(username, new BCryptPasswordEncoder().encode(password), authorityList);
    }

}
