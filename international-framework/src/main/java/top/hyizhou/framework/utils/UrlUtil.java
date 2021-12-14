package top.hyizhou.framework.utils;

import java.util.Arrays;

/**
 * URL工具类，使用时请不要带协议：如http：//localhost/a/ 只需要输入localhost/a/
 * @author huanggc
 * @date 2021/12/14 15:33
 */
public class UrlUtil {
    private static final String JOINER = "/";

    /**
     * 格式化url后缀，添加"/"
     * @param url 资源定位符
     * @return 处理完成后的url
     */
    public static String formatSuffix(String url){
        if (!url.endsWith(JOINER)) {
            url += JOINER;
        }
        return url;
    }

    /**
     * 解析url地址，将每个地址元素取出，注意：若是带有参数的地址，参数将被丢掉
     * @param url 资源定位符
     * @return 存储地址元素的数组
     */
    public static String[] analyze(String url){
        String[] split = url.split("/");
        String[] reply = new String[split.length];
        int index = 0;
        for (String s : split) {
            if (!"".equals(s)) {
                reply[index++] = s;
            }
        }
        reply[index-1] = reply[index-1].split("\\?")[0];
        return Arrays.copyOfRange(reply,0,index);
    }

    /**
     * 格式化url，主要是去除多余的/
     * @param url url地址
     * @return 格式化后的地址
     */
    public static String format(String url){
        StringBuilder strB = new StringBuilder(url);
        char joiner = JOINER.charAt(0);
        char last = '\0';
        for (int i = 0; i < strB.length(); i++) {
            // 判断上一次的字符和这一次字符是否都为'/'
            if (joiner == strB.charAt(i) && last == joiner){
                strB.delete(i, i+1);
                --i;
            }else {
                last = strB.charAt(i);
            }
        }
        return strB.toString();
    }

    public static void main(String[] args) {
        String a = "http:///a//dfsda/dafg///ghdf/g/gfdg//";
        System.out.println(format(a));
    }
}
