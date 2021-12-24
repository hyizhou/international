package top.hyizhou.framework.service;

import org.springframework.beans.factory.annotation.Autowired;
import top.hyizhou.framework.entity.UserInfoBean;
import top.hyizhou.framework.porxy.UserMapping;

/**
 * 处理登录验证相关信息
 * @author hyizhou
 * @date 2021/12/24 17:37
 */
public class LoginService {

    @Autowired
    private UserMapping userMapping;

    public boolean allowLogin(String userName, String password){
        UserInfoBean user = userMapping.select(userName);
        if (user == null){
            return false;
        }
        return user.getPassword().equals(password);
    }
}
