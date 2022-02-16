package top.hyizhou.framework.utils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 硬盘工具类
 * @author hyizhou
 * @date 2022/2/15 17:53
 */
public class DiskUtil {
    /**
     * 获得计算机盘符
     * @return 盘符列表
     */
    public static File[] getPartition(){
        return File.listRoots();
    }

    /**
     * 判断文件所在盘符是否有足够空间
     * @param file 文件对象
     * @param space 指定的空间数，单位byte
     * @return 若剩余空间少于指定大小，则会返回false
     */
    public static boolean hasEnoughSpace(File file, long space) throws FileNotFoundException {
        if (!file.exists()){
            throw new FileNotFoundException("路径不存在："+file.getPath());
        }
        long freeSpace = file.getFreeSpace();
        return freeSpace >= space;
    }

    /**
     * 判断文件所在盘符是否有足够空间
     * @param path 路径，可以是文件/目录，也可是盘符
     * @param space 空间大小，盘内空间大于此数则表示空间充足，单位byte
     * @return 空间充足返回true
     * @throws FileNotFoundException 路径错误
     */
    public static boolean hasEnoughSpace(String path, long space) throws FileNotFoundException {
        File file = new File(path);
        return hasEnoughSpace(file, space);
    }

}
