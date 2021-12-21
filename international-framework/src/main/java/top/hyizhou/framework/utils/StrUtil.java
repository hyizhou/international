package top.hyizhou.framework.utils;

import java.util.regex.Pattern;

/**
 * 字符串工具类
 * @author huanggc
 * @date 2021/12/14 16:56
 */
public class StrUtil {
    /** 判断字符串是否为整数的正则 */
    private static final Pattern INTEGER_PATTERN = Pattern.compile("^[-+]?[\\d]+$");

    /**
     * 删除某位置的一个字符
     * @param str 源字符串
     * @param index 需要删除的位置
     * @return 处理后的字符
     */
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

    /**
     * 查找指定字符在字符串中第指定次数出现的位置，目前方法性能不高
     * @param str 原始字符串
     * @param num 第几次出现，若小于等于0，则按1计算
     * @param ch 查找的字符
     * @return 位置，若不存在则返回-1
     */
    public static int indexOf(String str,String ch, int num){
        // 若num小于等于0，则默认为1
        if (num <= 0){
            num = 1;
        }
        int index = -1;
        for (int i = 0; i < num; i++) {
            int i1 = str.indexOf(ch);
            if (i1 < 0){
                // 进入这里表示还没有到指定次数，就已经不存在所搜索字符了
                return -1;
            }
            index += i1+1;
            str = str.substring(i1+1);
        }
        return index;
    }

    /**
     * 判断整个字符串是否为整数
     * @param str 待判断字符
     * @return 若是整数，无论正负都返回true，否则false
     */
    public static boolean isInteger(String str){
        return INTEGER_PATTERN.matcher(str).matches();
    }

    public static void main(String[] args) {
        String a = "+";
        System.out.println(isInteger(a));
    }

}
