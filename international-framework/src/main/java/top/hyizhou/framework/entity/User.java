package top.hyizhou.framework.entity;

import java.util.Date;

/**
 * 用户信息表
 * @author hyizhou
 * @date 2021/12/31 11:08
 */
public class User {
    private Integer id;
    private String name;
    private String password;
    private Date createTime;
    private Boolean isAdmin;
    private Boolean isDelete;
    private String email;
    private String phone;
    private String column1;
    private String column2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public String getColumn2() {
        return column2;
    }

    public void setColumn2(String column2) {
        this.column2 = column2;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", createTime=" + createTime +
                ", isAdmin=" + isAdmin +
                ", isDelete=" + isDelete +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", column1='" + column1 + '\'' +
                ", column2='" + column2 + '\'' +
                '}';
    }
}
