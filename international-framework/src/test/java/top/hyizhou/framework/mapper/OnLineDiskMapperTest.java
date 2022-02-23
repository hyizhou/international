package top.hyizhou.framework.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.hyizhou.framework.domain.OnLineDisk;

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
    public void insert(){
        OnLineDisk onLineDisk = new OnLineDisk();
        onLineDisk.setUserId(1);
        onLineDisk.setDirName("test2");
        onLineDisk.setAllSize(10240L);
        onLineDisk.setUseSize(0L);
        int i = mapper.insert(onLineDisk);
        System.out.println("插入条数："+i);
    }

    @Test
    public void update(){
        OnLineDisk date = mapper.findById(2);
        System.out.println(date);
        date.setUseSize(1L);
        int len = mapper.update(date);
        System.out.println("更新条数："+len);
    }
}
