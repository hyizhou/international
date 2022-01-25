package top.hyizhou.framework.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.hyizhou.framework.entity.User;

import java.util.List;

/**
 * UserMapper测试
 * @author hyizhou
 * @date 2022/1/25 11:53
 */
@SpringBootTest
public class UserMapperTest {
    @Autowired
    private UsersMapper mapper;
    @Test
    public void findByVagueTest(){
        User user = new User();
//        user.setName("小");
//        user.setPhone("123");
        List<User> userList = mapper.findByVague(user, 0, 100);
        int size = userList.size();
        System.out.println("size="+size);
        for (User user1 : userList) {
            System.out.println(user1);
        }
    }
}
