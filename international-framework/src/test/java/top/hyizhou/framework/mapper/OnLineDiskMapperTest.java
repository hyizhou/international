package top.hyizhou.framework.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.hyizhou.framework.pojo.OnLineDisk;

/**
 * @author hyizhou
 * @date 2022/1/27 17:18
 */
@SpringBootTest
public class OnLineDiskMapperTest {
    @Autowired
    private OnLineDiskMapper mapper;
    @Test
    public void findById(){
        OnLineDisk byId = mapper.findById(17);
        System.out.println(byId);
    }

    @Test
    public void insertOne(){
        OnLineDisk onLineDisk = new OnLineDisk();
        onLineDisk.setUserId(12);
        onLineDisk.setDirName("test1");
        onLineDisk.setAllSize(10240L);
        onLineDisk.setUseSize(0L);
        int i = mapper.insertOne(onLineDisk);
        System.out.println("插入条数："+i);
    }
}
