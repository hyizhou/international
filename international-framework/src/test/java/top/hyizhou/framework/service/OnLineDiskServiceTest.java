package top.hyizhou.framework.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author hyizhou
 * @date 2022/2/16 11:49
 */
@SpringBootTest
public class OnLineDiskServiceTest {
    @Autowired
    private OnLineDiskService service;
    @Test
    @DisplayName("异常测试")
    public void propertyTest(){
        ArithmeticException assertThrows = Assertions.assertThrows(ArithmeticException.class, () -> System.out.println(1 % 0));
        System.out.println(assertThrows.getMessage());
    }

}
