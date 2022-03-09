package top.hyizhou.framework.utils.onlinedisk;

import org.springframework.core.io.Resource;
import top.hyizhou.framework.entity.SimpleFileInfo;

import java.io.InputStream;
import java.util.List;

/**
 * 仓库对象，提供云盘操作仓库的接口
 * @author hyizhou
 * @date 2022/2/17 15:28
 */
public interface Warehouse {
    /**
     * 删除目录或文件
     * @param path 路径
     * @return 成功操作返回true
     */
    boolean delete(String path);

    /**
     * 创建目录
     * @param path 目录路径
     * @return 成功操作返回true
     */
    boolean mkdir(String path);

    /**
     * 文件保存，若有同名文件将覆盖
     * @param path 保存文件路径
     * @param in 输入流
     * @return 成功操作返回true
     */
    boolean save(String path, InputStream in);

    /**
     * 重命名文件或目录
     * @param path 路径
     * @param newName 新名称
     * @return 成功操作返回true
     */
    boolean rename(String path, String newName);

    /**
     * 移动文件或目录
     * @param path 原路径
     * @param newPath 新路径
     * @return 成功操作返回true
     */
    boolean move(String path, String newPath);

    /**
     * 复制文件或目录
     * @param path 原路径
     * @param targetPath 目标路径
     * @return 成功操作返回true
     */
    boolean copy(String path, String targetPath);

    /**
     * 获取文件/目录信息
     * @param path 路径
     * @return 文件信息
     */
    SimpleFileInfo getFileInfo(String path);

    /**
     * 获取文件
     * @param path 路径
     * @return 以流的形式返回
     */
    Resource getFile(String path);

    /**
     * 获取子文件
     * @param path 路径
     * @return 简单文件信息对象列表
     */
    List<SimpleFileInfo> listFilesInfo(String path);
}
