package top.hyizhou.framework.entity;

/**
 * 共享文件详情
 * @author hyizhou
 * @date 2022/2/20
 */
public class SharedFileDetail extends OnlinediskFileDetail{
    /** 下载次数 */
    private int downloadCount;
    /** 共享结束时间 */
    private long sharedTime;

    public SharedFileDetail(){
        this.setShear(true);
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public long getSharedTime() {
        return sharedTime;
    }

    public void setSharedTime(long sharedTime) {
        this.sharedTime = sharedTime;
    }
}
