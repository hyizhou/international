package top.hyizhou.framework.entity;

/**
 * 用户信息
 *
 * @author hyizhou
 * @date 2021/12/24 17:40
 */
public class UserInfoBean {
    private String userName;
    private String password;

    public UserInfoBean(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
