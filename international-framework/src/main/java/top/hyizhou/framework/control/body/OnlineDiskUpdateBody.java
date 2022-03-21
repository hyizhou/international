package top.hyizhou.framework.control.body;

/**
 * @author huanggc
 * @date 2022/3/21 17:37
 */
public class OnlineDiskUpdateBody {
    private String oldPath;
    private String targetPath;
    /** 操作类型，1，重命名， 2，移动， 3，复制 */
    private int type;

    public String getOldPath() {
        return oldPath;
    }

    public void setOldPath(String oldPath) {
        this.oldPath = oldPath;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
