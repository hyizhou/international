package top.hyizhou.framework.domain;

/**
 * onlinedisk表对应的实体类
 * @author hyizhou
 * @date 2022/1/27 16:19
 */
public class OnLineDisk {
    private Integer userId;
    private String dirName;
    private Long allSize;
    private Long useSize;
    private Boolean shutoff;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public Long getAllSize() {
        return allSize;
    }

    public void setAllSize(Long allSize) {
        this.allSize = allSize;
    }

    public Long getUseSize() {
        return useSize;
    }

    public void setUseSize(Long useSize) {
        this.useSize = useSize;
    }

    public Boolean getShutoff() {
        return shutoff;
    }

    public void setShutoff(Boolean shutoff) {
        this.shutoff = shutoff;
    }

    @Override
    public String toString() {
        return "OnLineDisk{" +
                "userId=" + userId +
                ", dirName=" + dirName +
                ", allSize=" + allSize +
                ", useSize=" + useSize +
                ", shutoff=" + shutoff +
                '}';
    }
}
