package top.hyizhou.framework.utils.onlinedisk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hyizhou.framework.entity.SimpleFileInfo;
import top.hyizhou.framework.utils.FilesUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 存储在本地硬盘上的仓库
 * @author hyizhou
 * @date 2022/2/17 15:48
 */
public class LocalWarehouse implements Warehouse {
    private final Logger logger = LoggerFactory.getLogger(LocalWarehouse.class);
    /** 仓库目录路径，后续方法中的路径皆是相对于本路径的 */
    private final String root;

    public LocalWarehouse(String root) {
        this.root = root;
    }

    @Override
    public boolean delete(String path) {
        path = FilesUtil.join(root, path);
        try {
            FilesUtil.rm(path, logger.isDebugEnabled());
        } catch (IOException e) {
            logger.error("仓库删除操作执行失败 -- path={}", path);
            logger.error("", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean mkdir(String path) {
        return false;
    }

    @Override
    public boolean save(String path, InputStream in) {
        return false;
    }

    @Override
    public boolean rename(String path, String newName) {
        return false;
    }

    @Override
    public boolean move(String path, String newPath) {
        return false;
    }

    @Override
    public SimpleFileInfo getFileInfo(String path) {
        SimpleFileInfo info = new SimpleFileInfo();
        File file = new File(FilesUtil.join(root, path));
        try {
            info.setLength(FilesUtil.size(file));
        } catch (IOException e) {
            logger.error("仓库获取文件/目录信息失败 -- path={}", path);
            logger.error("", e);
            return null;
        }
        info.setDirectory(file.isDirectory());
        info.setName(file.getName());
        return info;
    }

    @Override
    public InputStream getFile(String path) {
        return null;
    }
}
