package top.hyizhou.framework.utils;

import java.io.File;

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
    public static boolean hasEnoughSpace(File file, long space){
        long freeSpace = file.getFreeSpace();
        return freeSpace >= space;
    }
}
