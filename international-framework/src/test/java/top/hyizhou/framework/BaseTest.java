package top.hyizhou.framework;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 基本测试类
 * @author hyizhou
 * @date 2022/2/15 18:01
 */
public class BaseTest {
    public static void main(String[] args) {
        Path path = Paths.get("/aa/bb/cc/dd/e/");
        System.out.println(path.getNameCount());
        for (int i = 0; i < path.getNameCount(); i++) {
            System.out.println("e".equals(path.getName(i).toString()));
        }
    }
    @Test
    public void test(){
        User user = (User) User.withDefaultPasswordEncoder()
                .username("user")
                .password("1234")
                .roles("user")
                .build();
        System.out.println(user.getPassword());
    }
}
