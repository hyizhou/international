package top.hyizhou.framework.entity;

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
    public SimpleFileInfo(){}

    public SimpleFileInfo(String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
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

}
