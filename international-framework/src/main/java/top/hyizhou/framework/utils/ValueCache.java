package top.hyizhou.framework.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 变量缓存
 * @author huanggc
 * @date 2021/11/25 15:01
 */
public class ValueCache {
    private static final Map<String, Cache> MAP;
    static {
        MAP = new HashMap<>();
    }

    public static Object get(String key){
        Cache cache = ValueCache.MAP.get(key);
        if (cache == null) {
            return null;
        }
        if (cache.isExpired()){
            // 若过期，则删除
            ValueCache.MAP.remove(key);
            return null;
        }
        return cache.getValue();
    }

    /**
     * 放入永久变量，不会过期丢弃
     * @param key 键值
     * @param value 变量值
     */
    public static void set(String key, Object value){
        Cache cache = new Cache(value, -1);
        ValueCache.MAP.put(key, cache);
    }

    /**
     * 放入新的临时变量，若之前已经放入相同key值的变量，将会被覆盖
     * @param key 键值，需要通过key取出
     * @param value 变量值
     * @param subsistTime 存活时间，单位秒数
     */
    public static void set(String key, Object value, int subsistTime){
        Cache cache = new Cache(value, subsistTime);
        ValueCache.MAP.put(key, cache);
    }

    /**
     * 更新变量值，若变量过期或根本不存在，则会产生空指针异常
     * @param key key值
     * @param value 新的变量值
     */
    public static void update(String key, Object value){
        Cache cache = MAP.get(key);
        if (cache == null || cache.isExpired()){
            throw new NullPointerException("指定的键为空:key="+key);
        }
        cache.setValue(value);
    }

    /**
     * 删除
     * @param key 键值
     */
    public static void remove(String key){
        MAP.remove(key);
    }

    private static class Cache{
        /** 存储的变量 */
        private Object value;
        /** 存活时间，单位：秒, 若为-1表示一直存在*/
        private int subsistTime;
        /** 构造时间，单位：毫秒 */
        private final long constructTime;

        public Cache(Object value, int subsistTime){
            this.value = value;
            this.subsistTime = subsistTime;
            this.constructTime = System.currentTimeMillis();
        }
        private boolean isExpired(){
            if (subsistTime == -1){
                return false;
            }
            long time = System.currentTimeMillis();
            return time - constructTime > subsistTime * 1000L;
        }

        private Object getValue(){
            return this.value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        private int getSubsistTime(){
            return subsistTime;
        }

        public void setSubsistTime(int subsistTime) {
            this.subsistTime = subsistTime;
        }
    }

}
