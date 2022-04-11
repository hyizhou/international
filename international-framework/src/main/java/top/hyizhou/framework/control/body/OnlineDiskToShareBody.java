package top.hyizhou.framework.control.body;

/**
 * 用户将文件/目录进行分享时的配置信息
 * @author huanggc
 * @date 2022/4/8 9:27
 */
public class OnlineDiskToShareBody {
    /** 分享路径 */
    private String path;
    /** 分享多久时间，理论上应提供选项：1、3、5、7、30天、永久 供选择，而不是用户自己输入秒数 */
    private Integer time;
    /** 分享密码，若不设密码则为null */
    private String passwd;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
