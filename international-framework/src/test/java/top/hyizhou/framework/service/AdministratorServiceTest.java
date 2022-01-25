package top.hyizhou.framework.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.hyizhou.framework.entity.User;

/**
 * AdministratorService类测试
 * @author hyizhou
 * @date 2022/1/25 16:16
 */
@SpringBootTest
public class AdministratorServiceTest {
    @Autowired
    private AdministratorService service;

    @Test
    public void deleteUser(){
        User user = new User();
        user.setId(2);
        service.deleteUser(user);
    }

    @Test
    public void receiveUser(){
        User user = new User();
        user.setId(2);
        service.receiveUser(user);
    }
}
