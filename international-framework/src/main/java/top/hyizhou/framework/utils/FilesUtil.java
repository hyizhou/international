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
     * @param file 删除文件对象
     * @param log 是否将删除文件记录在log中
     */
    public static void rm(File file, boolean log) throws IOException {
        Path path = file.toPath();
        Files.walkFileTree(path, new RemoveFolderVisitor(log));
    }

    /**
     * 删除文件或目录
     * @param file 文件对象
     * @throws IOException io错误
     */
    public static void rm(File file) throws IOException {
        rm(file, false);
    }

    /**
     * 将多个目录拼接成完整路径
     * top:使用path可以不同这么麻烦
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

    /**
     * 文件/目录移动(或重命名)，将原文件或目录移动到新位置，若新位置存在文件或空目录将被覆盖。
     * 若是移动目录时，目标目录已经存在且不为空，则无法完成
     * @param oldFile 旧文件
     * @param newFile 新文件
     */
    public static void mv(File oldFile, File newFile) throws IOException {
        Files.move(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 复制文件/目录到指定位置，保留源文件属性，如创建日期
     * @param from 源文件/目录
     * @param target 目标文件/目录
     */
    public static void cp(File from, File target) throws IOException {
        // 目标父目录
        File targetParentFile = target.getParentFile();
        // 目标不是根目录，且父目录不存在，则进行创建
        if (targetParentFile != null && !targetParentFile.exists()) {
            if (!targetParentFile.mkdirs()) {
                throw new IOException("创建目标父目录失败  path："+targetParentFile.getAbsolutePath());
            }
        }
        Files.walkFileTree(from.toPath(), new CopyFolderVisitor(from.toPath(), target.toPath()));
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

    /**
     * 复制文件树，将覆盖目标目录同名文件，复制后的文件目录保持原属性，如创建日期与原文件一致（修改日期可能变了）
     */
    private static class CopyFolderVisitor extends SimpleFileVisitor<Path>{
        private final Path fromPath;
        private final Path toPath;

        public CopyFolderVisitor(Path fromPath, Path toPath) {
            this.fromPath = fromPath;
            this.toPath = toPath;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.copy(file, toPath.resolve(fromPath.relativize(file)), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            Path targetPath = toPath.resolve(fromPath.relativize(dir));
            if (!Files.exists(targetPath)){
                Files.copy(dir, targetPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
            }
            return FileVisitResult.CONTINUE;
        }
    }

}
