package top.hyizhou.framework.utils.container;

import top.hyizhou.framework.entity.User;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储用户登录状态，已经登录用户特征值将存储在此容器中
 * @author hyizhou
 * @date 2022/1/25 17:50
 */
public class LoggedOnContainer {
    private final Map<String, User> map;

    public LoggedOnContainer(){
        map = new HashMap<>();
    }

    /**
     * 判断key是否存在
     * @param key key
     * @return 存在则返回true
     */
    public boolean exist(String key){
        return map.containsKey(key);
    }

    /**
     * 添加登录的用户
     * @param key 存入cookie的key
     * @param user 用户实体对象
     */
    public void addLoginUser(String key, User user){}

    /**
     * 删除登录状态
     * @param key key
     */
    public void removeLoginUser(String key){}
}
