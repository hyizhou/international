package top.hyizhou.framework.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 简单文件信息
 * @author hyizhou
 * @date 2021/12/7 11:48
 */
public class SimpleFileInfo {
    /** 文件名 */
    private String name;
    /** 是否为目录 */
    private boolean isDirectory;
    /** 文件大小 */
    private Long length;
    /** 文件最后修改时间，使用字符串存储为方便前端直接显示 */
    private long lastModified;
    public SimpleFileInfo(){}

    public SimpleFileInfo(String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
    }

    public static List<SimpleFileInfo> createSimpleFileInfo(File[] files){
        if (files == null){
            return new ArrayList<>();
        }
        // 也许可以换成流式写法
        List<SimpleFileInfo> req = new ArrayList<>();
        for (File file : files) {
            SimpleFileInfo simpleFileInfo = new SimpleFileInfo(file.getName(), file.isDirectory());
            simpleFileInfo.setLength(file.length());
            simpleFileInfo.setLastModified(file.lastModified());
            req.add(simpleFileInfo);
        }
        return req;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}
