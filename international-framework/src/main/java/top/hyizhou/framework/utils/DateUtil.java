package top.hyizhou.framework.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * @author hyizhou
 * @date 2022/1/26 17:21
 */
public class DateUtil {
    /**
     * 根据当前时间，增加一定的时间
     * @param second 增加的时间，单位为秒
     * @return 时间
     */
    public static Date addDate(int second){
        Date nowDate = new Date();
        return new Date(nowDate.getTime() + (second* 1000L));
    }

    public static void main(String[] args) {
        Date date = addDate(30);
        System.out.println(date);
        System.out.println(new Date());
        System.out.println(new Date().before(date));
        System.out.println(new Date().after(date));
    }

    /**
     * 获取方便阅读的当前时间
     * @return 当前时间格式化后形式
     */
    public static String currentTimeReadable(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }
}
