package top.hyizhou.framework.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import top.hyizhou.framework.entity.User;
import top.hyizhou.framework.except.OnLineDiskException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author hyizhou
 * @date 2022/2/16 11:49
 */
@SpringBootTest
public class OnLineDiskServiceTest {
    @Autowired
    private OnLineDiskService service;
    @Autowired AccountService accountService;
    private User testUser;

    @BeforeEach
    public void getTestUser(){
        if (testUser == null) {
            User user = accountService.findUser(21, null);
            assert user != null : "无用户";
            this.testUser = user;
        }
    }
    @Test
    @DisplayName("异常测试")
    public void propertyTest(){
        ArithmeticException assertThrows = Assertions.assertThrows(ArithmeticException.class, () -> System.out.println(1 % 0));
        System.out.println(assertThrows.getMessage());
    }

    @Test
    public void createOnlineDisk(){
        service.create(testUser);
    }

    @Test
    public void delete(){
        System.out.println("删除云盘测试开始");
        boolean isOk = service.delete(testUser);
        System.out.println("删除成功吗？   "+isOk);
        System.out.println("删除云盘测试结束");
    }

    @Test
    public void recover(){
        System.out.println("恢复开始");
        boolean recover = service.recover(testUser);
        System.out.println("恢复成功？  "+recover);
        System.out.println("恢复结束");
    }

    @Test
    public void clear(){
        System.out.println("清理方法测试开始");
        boolean clear = service.clear(testUser);
        System.out.println("清理成功了吗？   "+clear);
        System.out.println("清理方法测试结束");
    }

    @Test
    public void saveFile() throws IOException {
        System.out.println("文件存储方法测试开始");
        File file = new File("D:/cc.txt");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
        try {
            service.saveFile(testUser, multipartFile, "/test/bb");
        } catch (OnLineDiskException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("成功");
        System.out.println("文件存储方法测试解释");
    }

    @Test
    public void rmPath() throws OnLineDiskException {
        System.out.println("文件删除方法测试开始");
        boolean b = service.rmPath(testUser, "");
        System.out.println("文件删除成功？ "+b);
        System.out.println("文件删除方法测试结束");

    }

}
