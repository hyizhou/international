package top.hyizhou.framework.utils.container;

import net.minidev.json.JSONUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.hyizhou.framework.config.constant.CookieConstant;
import top.hyizhou.framework.entity.User;

import java.util.Map;

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

    @Test
    public void clear() throws InterruptedException {
        loggedOnContainer.addLoginUser(1, CookieConstant.SIGN_MAX_AGE);
        loggedOnContainer.addLoginUser(2, 1);
        loggedOnContainer.addLoginUser(1, 3);
        loggedOnContainer.addLoginUser(4, 1);
        Map<String, LoggedOnContainer.LoggedOnInfo> all = loggedOnContainer.getAll();
        all.forEach((k,v) -> {
            System.out.println(k+":"+v);
        });
        Thread.sleep(2000);
        long startTime = System.nanoTime();
        int clearLen = loggedOnContainer.clear();
        System.out.println("清理耗时："+(System.nanoTime()-startTime)+"ns");
        System.out.println("清理"+clearLen+"条");
        loggedOnContainer.getAll().forEach((k,v) -> {
            System.out.println(k+":"+v);
        });

    }
}
