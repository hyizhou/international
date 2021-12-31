package top.hyizhou.framework.mapper;

import top.hyizhou.framework.entity.UserInfo;

/**
 * 用户信息数据库操作
 * @author hyizhou
 * @date 2021/12/27 10:35
 */
public interface UserMapping {

    /** 通过用户名查询用户 */
    UserInfo select(String userName);

    /** 添加新用户 */
    void insert(UserInfo bean);

    /** 通过用户名删除用户 */
    boolean delete(String userName);
}
