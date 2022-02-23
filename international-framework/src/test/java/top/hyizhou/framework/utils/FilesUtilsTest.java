package top.hyizhou.framework.utils;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

/**
 * @author hyizhou
 * @date 2022/2/23 16:51
 */
public class FilesUtilsTest {

    @Test
    public void test(){
        String path = "/a/b/c";
        String path2 = "/a/b/c/d/e";
        System.out.println(Paths.get(path).relativize(Paths.get(path2)));
    }
}
