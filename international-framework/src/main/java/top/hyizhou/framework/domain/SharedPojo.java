package top.hyizhou.framework.domain;

import java.util.Date;

/**
 * shared表对应实体类
 * @author hyizhou
 * @date 2022/1/28 15:48
 */
public class SharedPojo {
    /** 主键 */
    private Integer id;
    /** 用户id */
    private Integer userId;
    private String path;
    private Boolean isFile;
    private Integer infoId;
    private Date sharedTime;
    /** 下载次数 */
    private Integer nuDown;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getIsFile() {
        return isFile;
    }

    public void setIsFile(Boolean file) {
        isFile = file;
    }

    public Integer getInfoId() {
        return infoId;
    }

    public void setInfoId(Integer infoId) {
        this.infoId = infoId;
    }

    public Date getSharedTime() {
        return sharedTime;
    }

    public void setSharedTime(Date sharedTime) {
        this.sharedTime = sharedTime;
    }

    public Integer getNuDown() {
        return nuDown;
    }

    public void setNuDown(Integer nuDown) {
        this.nuDown = nuDown;
    }

    @Override
    public String toString() {
        return "SharedPojo{" +
                "id=" + id +
                ", userId=" + userId +
                ", path='" + path + '\'' +
                ", isFile=" + isFile +
                ", infoId=" + infoId +
                ", sharedTime=" + sharedTime +
                ", nuDown=" + nuDown +
                '}';
    }
}
