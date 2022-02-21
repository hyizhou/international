package top.hyizhou.framework.utils.onlinedisk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import top.hyizhou.framework.entity.SimpleFileInfo;
import top.hyizhou.framework.utils.FilesUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * 存储在本地硬盘上的仓库
 * 仓库结构：云盘主仓库 -- 用户仓库 -- 具体路径
 * 如：D:/warehouse/userHouse/book/aa.txt；warehouse为主仓库，userHouse为用户仓库
 * @author hyizhou
 * @date 2022/2/17 15:48
 */
public class LocalWarehouse implements Warehouse {
    private final Logger logger = LoggerFactory.getLogger(LocalWarehouse.class);
    /** 主仓库目录路径 */
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
        path = FilesUtil.join(root, path);
        File file = new File(path);
        if (file.exists()) {
            logger.error("仓库创建目录失败 -- 目录已存在：{}", path);
            return false;
        }
        return file.mkdirs();
    }

    @Override
    public boolean save(String path, InputStream in) {
        path = FilesUtil.join(root, path);
        File file = new File(path);
        if (file.exists() && file.isDirectory()){
            logger.error("仓库保存文件失败 -- 保存路径为目录：{}", path);
            return false;
        }
        // 创建文件，若文件存在则使用存在的文件
        try {
            Files.createFile(FileSystems.getDefault().getPath(path));
        } catch (FileAlreadyExistsException e) {
            logger.error("仓库保存文件失败 -- 创建文件时文件已经存在");
            return false;
        } catch (IOException e){
            logger.error("仓库保存文件失败 -- 发生IO错误，或父目录不存在");
            return false;
        }
        // 若输入流为null，则创建空白文件
        if (in == null){
            logger.info("仓库保存文件提示 -- 输入流为null，因此只创建了文件，而未写入任何数据");
            return true;
        }
        // TODO 存在输入流
        logger.info("仓库保存文件提示 -- 输入流处理部分暂时没写完");
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
        File file = new File(FilesUtil.join(root, path));
        return getFileInfo(file);
    }

    private SimpleFileInfo getFileInfo(File file){
        SimpleFileInfo info = new SimpleFileInfo();
        try {
            info.setLength(FilesUtil.size(file));
        } catch (IOException e) {
            logger.error("仓库获取文件/目录信息失败 -- path={}", file.getAbsolutePath());
            logger.error("", e);
            return null;
        }
        info.setDirectory(file.isDirectory());
        info.setName(file.getName());
        info.setLastModified(file.lastModified());
        return info;
    }

    @Override
    public Resource getFile(String path) {
        Path pathObj = FileSystems.getDefault().getPath(FilesUtil.join(root, path));
        Resource resource = new FileSystemResource(pathObj);
        if (!resource.exists()) {
            logger.error("仓库读取文件失败 -- 资源路径不存在：{}", path);
            return null;
        }
        return resource;
    }

    @Override
    public List<SimpleFileInfo> ListFilesInfo(String path) {
        path = FilesUtil.join(root, path);
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()){
            logger.error("仓库读取目录列表失败 -- 目录不存在或路径不是目录 -- path=[{}]",path);
            return null;
        }
        List<SimpleFileInfo> infos = new ArrayList<>();
        File[] files = file.listFiles();
        if (files == null){
            logger.error("仓库读取目录列表失败 -- 读取时发生io错误 -- path=[{}]", path);
            return null;
        }
        for (File subFile : files) {
            infos.add(getFileInfo(subFile));
        }
        return infos;
    }
}
