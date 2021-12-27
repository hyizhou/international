package top.hyizhou.framework.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import top.hyizhou.framework.entity.UserInfoBean;
import top.hyizhou.framework.mapping.UserMapping;

/**
 * 处理登录验证相关信息
 * @author hyizhou
 * @date 2021/12/24 17:37
 */
@Service
public class LoginService {

    private final UserMapping userMapping;

    public LoginService(@Qualifier("userMapping") UserMapping userMapping) {
        this.userMapping = userMapping;
    }

    /**
     * 判断用户名和密码，是否运行用户登录
     * @param userName 用户名
     * @param password 密码
     * @return 若是true则允许登录
     */
    public boolean allowLogin(String userName, String password){
        UserInfoBean user = userMapping.select(userName);
        if (user == null){
            return false;
        }
        return user.getPassword().equals(password);
    }
}