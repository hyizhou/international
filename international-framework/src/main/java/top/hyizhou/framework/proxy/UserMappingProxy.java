package top.hyizhou.framework.proxy;

import top.hyizhou.framework.entity.UserInfoBean;
import top.hyizhou.framework.mapping.UserMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * user数据库的代理，假装有一些数据
 * @author hyizhou
 * @date 2021/12/24 17:47
 */
public class UserMappingProxy implements UserMapping {
    private final List<UserInfoBean> userInfoList;

    public UserMappingProxy(){
        userInfoList = new ArrayList<>();
        userInfoList.add(new UserInfoBean("123", "1234"));
        userInfoList.add(new UserInfoBean("admin", "admin"));
    }

    /**
     * 通过用户名称查询
     * @param userName 用户名
     * @return 若咩有匹配到的则返回null
     */
    @Override
    public UserInfoBean select(String userName){
        for (UserInfoBean userInfoBean : userInfoList) {
            if (userInfoBean.getUserName().equals(userName)) {
                return userInfoBean;
            }
        }
        return null;
    }

    @Override
    public void insert(UserInfoBean bean){
        userInfoList.add(bean);
    }

    /**
     * 删除某个字段
     * @param userName 用户名
     * @return 删除成功则返回true
     */
    @Override
    public boolean delete(String userName){
        return userInfoList.removeIf(userInfoBean -> userInfoBean.getUserName().equals(userName));
    }
}
