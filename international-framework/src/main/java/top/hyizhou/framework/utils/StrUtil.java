package top.hyizhou.framework.utils;

/**
 * 字符串工具类
 * @author huanggc
 * @date 2021/12/14 16:56
 */
public class StrUtil {
    public static String delete(String str, int index){
        // 若给定字符串为空，则返回null
        if (isEmpty(str)){
            return null;
        }
        // index 取值范围为-len到len-1
        if (-str.length() > index || str.length() <= index){
            throw new StringIndexOutOfBoundsException(index);
        }
        if (index < 0){
            index = str.length() + index;
        }
        StringBuilder strBuilder = new StringBuilder(str);
        strBuilder.delete(index, index+1);
        return strBuilder.toString();
    }

    /**
     * 判断字符串为空
     * @param str 字符串
     * @return 若是空则返回true
     */
    public static boolean isEmpty(CharSequence str){
        return str == null || str.length() == 0;
    }


}
