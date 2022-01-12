package top.hyizhou.framework;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import top.hyizhou.framework.entity.User;
import top.hyizhou.framework.service.AccountService;

/**
 * 测试AccountService
 * @author hyizhou
 * @date 2022/1/4 15:24
 */
@SpringBootTest
public class AccountServiceTest {
    @Autowired
    private AccountService service;

    @Test
    public void testFindUser(){
        User user = service.findUser(2, null);
        assert user.getId() == 2;

        user = service.findUser(null, "test1");
        assert user.getAccountName().equals("test1");

        user = service.findUser(2, "test1");
        Assert.state(user.getId() == 2 && user.getAccountName().equals("test1"), "finduser方法测试异常");
    }

    @Test
    public void testIsRepeat(){
        boolean isRespeat = service.isRepeat("test1");
        Assert.state(isRespeat == true, "账户名判重与预期不一致");

        isRespeat = service.isRepeat("test1asdfas");
        System.out.println(isRespeat);
        Assert.state(isRespeat == false, "账户名判重与预期不一致");
    }

    @Test
    public void testRegister(){
        User user = new User();
        user.setName("test");
        user.setAccountName("test10086");
        user.setPassword("123456");
    }
}
