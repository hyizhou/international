package top.hyizhou.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * 文件操作工具类
 * @author hyizhou
 * @date 2022/2/16 15:52
 */
public class FilesUtil {
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

}
