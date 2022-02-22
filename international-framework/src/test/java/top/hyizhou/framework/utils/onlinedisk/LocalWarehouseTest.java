package top.hyizhou.framework.utils.onlinedisk;

import org.junit.jupiter.api.Test;

import java.io.*;

/**
 * @author hyizhou
 * @date 2022/2/22 15:22
 */
public class LocalWarehouseTest {
    @Test
    public void save() throws FileNotFoundException {
        LocalWarehouse warehouse = new LocalWarehouse("D:\\文件测试文件夹\\warehouse");
        InputStream in = new BufferedInputStream(new FileInputStream(new File("D:/文件测试文件夹/1640156309005733-2-hdtune_255 (1).exe")));
        String savePath = "saveTest.exe";
        warehouse.save(savePath, in);
    }
}
