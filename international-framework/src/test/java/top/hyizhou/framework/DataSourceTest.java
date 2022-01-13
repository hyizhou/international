package top.hyizhou.framework;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

/**
 * @author hyizhou
 * @date 2022/1/13 11:43
 */
@SpringBootTest
public class DataSourceTest {
    @Autowired
    private DataSource dataSource;

    /**
     * 查看数据源类型
     */
    @Test
    public void dataSourceType(){
        System.out.println(dataSource.getClass().getName());
    }
}
