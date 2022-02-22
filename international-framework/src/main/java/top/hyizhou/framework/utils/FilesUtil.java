package top.hyizhou.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * 文件操作工具类
 * @author hyizhou
 * @date 2022/2/16 15:52
 */
public class FilesUtil {
    /** 分隔符 */
    public static String separator = "/";
    /** windows下分隔符 */
    public static String winSeparator = "\\";
    /**
     * 递归删除文件或目录
     */
    public static void rm(File file, boolean log) throws IOException {
        Path path = file.toPath();
        Files.walkFileTree(path, new RemoveFolderVisitor(log));
    }

    /**
     * 递归删除目录或文件
     * @param file 文件路径
     * @throws IOException 删除失败异常
     */
    public static void rm(String file, boolean log) throws IOException {
        rm(new File(file), log);
    }

    public static void rm(String file) throws IOException {
        rm(file, false);
    }

    /**
     * 将多个目录拼接成完整路径
     * @param dirs 目录
     * @return 拼接后的路径
     */
    public static String join(String... dirs){
        StringBuilder str = new StringBuilder();
        if (dirs == null){
            return "";
        }
        int i = 0;
        String tmp;
        if (dirs.length > 0){
            tmp = dirs[0];
            tmp = tmp.replace('\\', '/');
            str.append(tmp);
            i++;
        }
        for (; i < dirs.length; i++) {
            tmp = dirs[i];
            if (StrUtil.isEmpty(tmp)){
                continue;
            }
            // 将可能出现的斜杠分隔符做替换
            tmp = tmp.replace('\\', '/');
            // 前缀已经带有分隔符
            if (tmp.charAt(0) == '/') {
                str.append(tmp);
            } else {
                str.append(separator).append(tmp);
            }
        }
        return str.toString();
    }

    /**
     * 获取目录或文件大小
     * @return 大小，单位byte
     */
    public static long size(File file) throws IOException {
        if (file.isFile()) {
            return file.length();
        }
        SizeOfFolderVisitor visitor = new SizeOfFolderVisitor();
        Files.walkFileTree(file.toPath(), visitor);
        return visitor.getSize();
    }

    public static long size(String path) throws IOException{
        return size(new File(path));
    }

    /**
     * 文件/目录移动，就是将旧文件移动到新文件位置，若新位置存在文件将被覆盖
     * TODO 目录移动部分待完成
     * @param oldFile 旧文件
     * @param newFile 新文件
     */
    public static void vm(File oldFile, File newFile){
        try {
            Files.move(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于递归删除目录
     */
    private static class RemoveFolderVisitor extends SimpleFileVisitor<Path>{
        private final Logger logger = LoggerFactory.getLogger(RemoveFolderVisitor.class);
        /** 是否在删除文件时将删除文件打印在日志 */
        private final boolean printLog;

        public RemoveFolderVisitor(boolean printLog) {
            this.printLog = printLog;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            if (printLog){
                logger.info("删除文件 -- {}", file);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            if (exc == null){
                Files.delete(dir);
                if (printLog){
                    logger.info("删除目录 -- {}", dir);
                }
                return FileVisitResult.CONTINUE;
            }
            throw exc;
        }
    }

    /**
     * 递归获取目录大小
     */
    private static class SizeOfFolderVisitor extends SimpleFileVisitor<Path>{
        private long size = 0;

        public long getSize() {
            return size;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            size += attrs.size();
            return FileVisitResult.CONTINUE;
        }
    }

    public static void main(String[] args) throws IOException {
        String path = "D:\\文件测试文件夹";
        System.out.println(size(new File(path)));
    }

}
