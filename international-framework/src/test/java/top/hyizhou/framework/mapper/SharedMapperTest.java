package top.hyizhou.framework.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.hyizhou.framework.pojo.SharedPojo;

import java.util.List;

/**
 * @author hyizhou
 * @date 2022/1/28 17:54
 */
@SpringBootTest
public class SharedMapperTest {
    @Autowired
    private SharedMapper mapper;

    @Test
    public void selectAll(){
        List<SharedPojo> all = mapper.findAll();
        for (SharedPojo sharedPojo : all) {
            System.out.println(sharedPojo);
        }
    }

    @Test
    public void insert(){
        SharedPojo pojo = new SharedPojo();
        pojo.setId(1);
        pojo.setIsFile(true);
        pojo.setPath("test1/aaa");
        pojo.setUserId(12);
        int len = mapper.insert(pojo);
        System.out.println("成功插入条数："+len);
    }
}
