package top.hyizhou.framework.utils.onlinedisk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import top.hyizhou.framework.entity.SimpleFileInfo;
import top.hyizhou.framework.utils.FilesUtil;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 存储在本地硬盘上的仓库
 * 仓库结构：云盘主仓库 -- 用户仓库 -- 具体路径
 * 如：D:/warehouse/userHouse/book/aa.txt；warehouse为主仓库，userHouse为用户仓库
 * 本类中方法所需参数path皆指相对主仓库的路径，即例子中"/userHouse/book/..."部分
 *
 * bug：../会被解析问题已在拦截器中解决
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
            FilesUtil.rm(new File(path), logger.isDebugEnabled());
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
        logger.info("仓库创建目录 -- path=[{}]", path);
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
            logger.error("仓库保存文件失败 -- 文件已经存在：{}", path);
            return false;
        } catch (IOException e){
            logger.error("仓库保存文件失败 -- 发生IO错误，或父目录不存在, path={}", path);
            return false;
        }
        // 若输入流为null，则创建空白文件
        if (in == null){
            logger.info("仓库保存文件提示 -- 输入流为null，因此只创建了文件，而未写入任何数据");
            return true;
        }
        // 存在输入流情况
        try(BufferedInputStream bufferIn = new BufferedInputStream(in);
            BufferedOutputStream bufferOut = new BufferedOutputStream(new FileOutputStream(file))
        ) {
            byte[] buffer = new byte[1000];
            int len;
            while ((len = bufferIn.read(buffer)) != -1) {
                bufferOut.write(buffer, 0, len);
            }
            bufferOut.flush();
            in.close();
            logger.info("仓库保存文件成功 -- path={}", file.getAbsolutePath());
            return true;
        } catch (IOException e) {
            logger.error("仓库保存文件失败 -- 流在保存过程中发生io错误");
            logger.error("", e);
            return false;
        }

    }

    @Override
    public boolean rename(String path, String newName) {
        path = FilesUtil.join(root, path);
        if (!new File(path).exists()){
            logger.error("路径不存在 -- path={}", path);
            return false;
        }
        try {
            FilesUtil.mv(new File(path), Paths.get(path).resolveSibling(newName).toFile());
        } catch (IOException e) {
            logger.error("仓库重命名失败");
            logger.error("", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean move(String path, String newPath) {
        path = FilesUtil.join(root, path);
        try {
            FilesUtil.mv(new File(path), Paths.get(root, newPath).toFile());
        } catch (IOException e) {
            logger.error("仓库移动失败");
            logger.error("", e);
            return false;
        }
        return true;
    }

    /**
     * 获取文件信息
     * @param path 路径
     * @return 返回文件信息对象，若范湖null，表示路径不存在
     */
    @Override
    public SimpleFileInfo getFileInfo(String path) {
        File file = new File(FilesUtil.join(root, path));
        try {
            file = new File(file.getCanonicalPath());
        } catch (IOException e) {
            logger.error("从路径[{}]获取规范路径失败，查看后面日志，若没报错则问题不大", file.getAbsolutePath());
        }
        if (!file.exists()){
            logger.error("路径[{}]不存在", file.getAbsolutePath());
            return null;
        }
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
        // TODO 应排除目录
        Path pathObj = FileSystems.getDefault().getPath(FilesUtil.join(root, path));
        Resource resource = new FileSystemResource(pathObj);
        if (!resource.exists()) {
            logger.error("仓库读取文件失败 -- 资源路径不存在：{}", path);
            return null;
        }
        return resource;
    }

    @Override
    public List<SimpleFileInfo> listFilesInfo(String path) {
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

    /**
     * 复制目录或文件到另一个位置
     */
    @Override
    public boolean copy(String path, String targetPath) {
        path = FilesUtil.join(root, path);
        targetPath = FilesUtil.join(root, targetPath);
        File pathFile = new File(path);
        File targetFile = new File(targetPath);
        if (!pathFile.exists()) {
            logger.error("复制仓库文件失败 -- 源路径不存在");
            return false;
        }
        try{
            FilesUtil.cp(pathFile, targetFile);
        }catch(IOException e){
            logger.error("仓库复制文件失败 -- io异常");
            logger.error("", e);
            return false;
        }
        return true;
    }
}
