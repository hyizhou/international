package top.hyizhou.framework;

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
}
