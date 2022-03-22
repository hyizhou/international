package top.hyizhou.framework.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import top.hyizhou.framework.entity.OnlinediskFileDetail;
import top.hyizhou.framework.entity.SimpleFileInfo;
import top.hyizhou.framework.entity.User;
import top.hyizhou.framework.except.OnLineDiskException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            User user = accountService.findUser(20, null);
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
    public void delete() throws OnLineDiskException {
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
    public void clear() throws OnLineDiskException {
        System.out.println("清理方法测试开始");
        boolean clear = service.clear(testUser);
        System.out.println("清理成功了吗？   "+clear);
        System.out.println("清理方法测试结束");
    }

    @Test
    public void saveFile() throws IOException {
        System.out.println("文件存储方法测试开始");
        List<String> fileList = new ArrayList<>();
        fileList.add("D:\\文件测试文件夹\\1640156309005733-2-hdtune_255 (1).exe");
        fileList.add("D:/cc.txt");
        for (String filePath : fileList) {
            File file = new File(filePath);
            FileInputStream inputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
            try {
                service.saveFile(testUser, multipartFile, "/写入文件");
            } catch (OnLineDiskException e) {
                e.printStackTrace();
                return;
            }
            System.out.println("成功 -- "+filePath);
        }

        System.out.println("文件存储方法测试解释");
    }

    @Test
    public void rmPath() throws OnLineDiskException {
        System.out.println("文件删除方法测试开始");
        service.rmPath(testUser, "");
        System.out.println("文件删除成功？ ");
        System.out.println("文件删除方法测试结束");

    }

    @Test
    public void getFile() throws OnLineDiskException, IOException {
        System.out.println("文件读取方法测试开始");
        Resource resource = service.getFile(testUser, "文件存储测试/bb/cc.txt");
        System.out.println(resource.contentLength());
        System.out.println(resource.getFilename());
        System.out.println(resource.isOpen());
        System.out.println("文件读取方法测试结束");
    }

    @Test
    public void getFileInfo() throws OnLineDiskException {
        System.out.println("获取文件详情测试开始");
        OnlinediskFileDetail fileDetail = service.getFileDetail(testUser, "文件存储测试/bb");
        System.out.println(fileDetail.isShear());
        System.out.println(fileDetail.getIsDirectory());
        System.out.println(fileDetail.getName());
        System.out.println("获取文件详情测试失败");
    }

    @Test
    public void listFolder() throws OnLineDiskException {
        System.out.println("获取目录内文件列表测试开始");
        List<SimpleFileInfo> simpleFileInfos = service.getFolderSub(testUser, "文件存储测试/bb");
        for (SimpleFileInfo simpleFileInfo : simpleFileInfos) {
            System.out.println(simpleFileInfo.getName());
        }
        System.out.println("获取目录内文件列表测试结束");
    }

    @Test
    public void mkdirOrFile() throws OnLineDiskException {
        System.out.println("创建目录或文件开始");
        service.mkdirOrFile(testUser, "写入文件/cc", true);
        System.out.println("创建目录或文件结束");
    }

    @Test
    public void sharedFile() throws OnLineDiskException {
        System.out.println("分享文件测试开始");
        // 分享一分钟
        service.shareFile(testUser, "文件存储测试/cc/aa.txt", 600);
        System.out.println("分享文件测试结束");
    }

    @Test
    public void getShare() throws OnLineDiskException {
        System.out.println("获取分享文件开始");
        Resource resource = service.getSharedFile("12/文件存储测试/cc/aa.txt");
        System.out.println(resource.getFilename());
        System.out.println("获取分享文件结束");
    }

    @Test
    public void getShareDetail() throws OnLineDiskException {
        System.out.println("获取分享文件详情测试开始");
        service.getSharedDetail("");
        System.out.println("获取分享文件详情测试结束");
    }
}
