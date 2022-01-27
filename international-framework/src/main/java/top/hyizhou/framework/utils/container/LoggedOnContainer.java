package top.hyizhou.framework.utils.container;

import org.springframework.util.DigestUtils;
import top.hyizhou.framework.utils.DateUtil;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 存储用户登录状态，已经登录用户特征值将存储在此容器中
 * @author hyizhou
 * @date 2022/1/25 17:50
 */
public class LoggedOnContainer {
    public static class LoggedOnInfo{
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

        @Override
        public String toString() {
            return "LoggedOnInfo{" +
                    "userId=" + userId +
                    ", date=" + date +
                    '}';
        }
    }

    /**
     * 存储已经登录用户，hash -- userInfo，
     * hash存储与浏览器cookie，userInfo为用户id与过期时间，此过期时间与cookie过期时间一般一致，
     * 当有用户重复登录时，会产生多个userInfo，这些userInfo并不一样，每个都为不同的过期时间，
     * 表示用户在不同设备上有着不同的登录时限
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
     * @return 用户实体对象id
     */
    public Integer getLoginUser(String key){
        return map.get(key).getUserId();
    }

    /**
     * 获取所有登录信息
     * @return map
     */
    public Map<String, LoggedOnInfo> getAll(){
        return map;
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
     * 清理容器中冗余信息，拥有线程锁，正在清理时，禁止对容器进行其他操作
     * @return 清理的信息条数
     */
    public synchronized int clear(){
        // 线程安全的包装类方便在lambda表达式中操作
        AtomicInteger len = new AtomicInteger(0);
        map.entrySet().removeIf(e -> {
            if (e.getValue().getDate().before(new Date())) {
                len.addAndGet(1);
                return true;
            }
            return false;
        });
        return len.get();
    }

    /**
     * 刷新存活时间
     */
    public void updateSurvivalTime(String key, Integer age){
        LoggedOnInfo info = map.get(key);
        info.setDate(DateUtil.addDate(age));
    }
}
