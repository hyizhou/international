package top.hyizhou.framework.mapping;

import top.hyizhou.framework.entity.UserInfoBean;

/**
 * 用户信息数据库操作
 * @author hyizhou
 * @date 2021/12/27 10:35
 */
public interface UserMapping {

    /** 通过用户名查询用户 */
    UserInfoBean select(String userName);

    /** 添加新用户 */
    void insert(UserInfoBean bean);

    /** 通过用户名删除用户 */
    boolean delete(String userName);
}
