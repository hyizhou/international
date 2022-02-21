package top.hyizhou.framework.entity;

/**
 * 云盘系统用来记录文件详情
 * @author hyizhou
 * @date 2022/2/18 14:53
 */
public class OnlinediskFileDetail extends SimpleFileInfo {
    /** 是分享的 */
    private boolean isShear;

    public boolean isShear() {
        return isShear;
    }

    public void setShear(boolean shear) {
        isShear = shear;
    }


}
