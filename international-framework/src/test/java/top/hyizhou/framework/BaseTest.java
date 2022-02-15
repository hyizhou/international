package top.hyizhou.framework;

import java.io.File;

/**
 * 基本测试类
 * @author hyizhou
 * @date 2022/2/15 18:01
 */
public class BaseTest {
    public static void main(String[] args) {
        File[] files = File.listRoots();
        for (File file : files) {
            System.out.println(file.getAbsolutePath());
            System.out.println(file.getFreeSpace());
            System.out.println(file.getTotalSpace());
        }
    }
}
