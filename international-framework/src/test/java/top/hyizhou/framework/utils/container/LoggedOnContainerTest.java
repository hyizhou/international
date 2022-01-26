package top.hyizhou.framework.utils.container;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.hyizhou.framework.config.constant.CookieConstant;
import top.hyizhou.framework.entity.User;

/**
 * 已登录信息容器测试
 * @author hyizhou
 * @date 2022/1/26 14:56
 */
@SpringBootTest
public class LoggedOnContainerTest {
    @Autowired
    private LoggedOnContainer loggedOnContainer;
    @Test
    public void buildKey(){
        for (int i = 0; i < 10; i++) {
            User user = new User();
            String s = loggedOnContainer.buildKey(user.getId());
            System.out.println(user.hashCode());
            System.out.println(s);
        }
    }

    @Test
    public void addLoggedUser(){
        User user = new User();
        user.setId(11);
        String key = loggedOnContainer.addLoginUser(user.getId(), CookieConstant.SIGN_MAX_AGE);
        System.out.println("存在与否："+loggedOnContainer.exist("12341345"));
        Integer userId = loggedOnContainer.getLoginUser(key);
        System.out.println("存储用户信息："+userId);
    }
}
