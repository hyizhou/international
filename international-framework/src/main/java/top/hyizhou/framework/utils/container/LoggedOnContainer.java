package top.hyizhou.framework.utils.container;

import org.springframework.util.DigestUtils;
import top.hyizhou.framework.utils.DateUtil;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 存储用户登录状态，已经登录用户特征值将存储在此容器中
 * @author hyizhou
 * @date 2022/1/25 17:50
 */
public class LoggedOnContainer {
    private static class LoggedOnInfo{
        /** 用户id */
        private Integer userId;
        /** 过期时间 */
        private Date date;

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }

    /**
     * 存储已经登录用户，hash -- userId
     * TODO 问题：若登录用户不主动注销，如何清除冗余信息。
     */
    private final Map<String, LoggedOnInfo> map;

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
     * 添加登录的用户到容器
     * @param key 存入cookie的key
     * @param userId 用户实体对象
     */
    public void addLoginUser(String key, Integer userId, Integer age){
        LoggedOnInfo info = new LoggedOnInfo();
        info.setUserId(userId);
        info.setDate(DateUtil.addDate(age));
        map.put(key, info);
    }

    /**
     * 添加登录的用户到容器，返回生成的key
     * @param userId 用户实体对象id
     * @return 返回存储在cookie和作为容器key中的字符
     */
    public String addLoginUser(Integer userId, Integer age){
        String key = buildKey(userId);
        LoggedOnInfo info = new LoggedOnInfo();
        info.setDate(DateUtil.addDate(age));
        info.setUserId(userId);
        map.put(key, info);
        return key;
    }

    /**
     * 删除登录状态
     * @param key key
     */
    public void removeLoginUser(String key){
        map.remove(key);
    }

    /**
     * 查询登录用户
     * @param key key
     * @return 用户实体对象
     */
    public Integer getLoginUser(String key){
        return map.get(key).getUserId();
    }

    /**
     * 通过用户实体对象生成对应的key
     * @param userId 用户实体对象
     * @return key
     */
    public String buildKey(Integer userId){
        String random = String.valueOf((int) (Math.random()*1000000));
        return DigestUtils.md5DigestAsHex((random + userId.hashCode()).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 获得容器大小
     * @return 容器大小
     */
    public int size(){
        return map.size();
    }

    /**
     * 清理容器中冗余信息
     * @return 清理的信息条数
     */
    public int clear(){
        List keyList = new ArrayList();
        map.entrySet().stream().filter(x-> x.getValue().getDate().after(new Date()))
                .map(e -> e.getKey());
        return 1;
    }
}
