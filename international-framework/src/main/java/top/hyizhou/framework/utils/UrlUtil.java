package top.hyizhou.framework.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * URL工具类</br>
 * 标准的URL由三部分组成：协议类型+主机名+路径及文件名，使用本工具类请不要带协议：如http：//localhost/a/ 只需要输入localhost/a/
 *
 * @author hyizhou
 * @date 2021/12/14 15:33
 */
public class UrlUtil {
    private static final String JOINER = "/";


    /**
     * 格式化url后缀，添加"/"
     *
     * @param url 资源定位符
     * @return 处理完成后的url
     */
    public static String formatSuffix(String url) {
        if (!url.endsWith(JOINER)) {
            url += JOINER;
        }
        return url;
    }

    /**
     * 对url地址使用'/'切片，将地址元素提取为数组，注意：若是带有参数的地址，参数将被丢掉
     *
     * @param url             资源定位符
     * @param ignoreParameter 是否忽略参数，true则忽略
     * @return 存储地址元素的数组
     */
    public static String[] split(String url, boolean ignoreParameter) {
        String[] split = url.split("/");
        String[] reply = new String[split.length];
        int index = 0;
        for (String s : split) {
            if (!"".equals(s)) {
                reply[index++] = s;
            }
        }
        if (ignoreParameter) {
            reply[index - 1] = reply[index - 1].split("\\?")[0];
        }
        return Arrays.copyOfRange(reply, 0, index);
    }

    /**
     * 将切片后的地址，重新拼接为完整URL
     *
     * @param address   切片后地址数组
     * @param hasPrefix 是否带上前缀'/'
     * @return 完整地址
     */
    public static String splice(String[] address, boolean hasPrefix, boolean hasSuffix) {
        StringBuilder ss = new StringBuilder();
        if (hasPrefix) {
            ss.append(JOINER);
        }
        for (String a : address) {
            ss.append(a).append(JOINER);
        }
        return hasSuffix ? ss.toString() : ss.delete(ss.length() - 1, ss.length()).toString();
    }

    /**
     * 格式化url，主要是去除多余的/
     *
     * @param url url地址
     * @return 格式化后的地址
     */
    public static String format(String url) {
        StringBuilder strB = new StringBuilder(url);
        char joiner = JOINER.charAt(0);
        char last = '\0';
        for (int i = 0; i < strB.length(); i++) {
            // 判断上一次的字符和这一次字符是否都为'/'
            if (joiner == strB.charAt(i) && last == joiner) {
                strB.delete(i, i + 1);
                --i;
            } else {
                last = strB.charAt(i);
            }
        }
        return strB.toString();
    }

    /**
     * 得到上一级url
     *
     * @param url 资源定位符/网址
     * @return 上一级资源地址，若返回null，则表示没有上一级或url为空
     */
    public static String parentUrl(String url) {
        // 判断是否为空，或是否为顶级目录
        if (StrUtil.isEmpty(url)) {
            return null;
        }
        String[] split = split(url, true);
        if (split.length == 1) {
            return null;
        }
        StringBuilder reply = new StringBuilder();
        // 若url开头有/，不能丢掉
        if (url.startsWith(JOINER)) {
            reply.append(JOINER);
        }
        for (int i = 0; i < split.length - 1; i++) {
            reply.append(split[i]).append(JOINER);
        }

        return reply.toString();
    }

    /**
     * 将请求地址的字符进行转义，默认utf8
     *
     * @param url Escape
     * @return 转义后地址
     */
    public static String encodeUrl(String url) {
        return encodeUrl(url, StandardCharsets.UTF_8.name());
    }

    /**
     * 将url转义。
     * bug：若带有参数会出现问题，因为参数部分的=号也会被转义
     *
     * @param url      原始url地址
     * @param encoding 指定编码格式
     * @return 转义后的url
     */
    public static String encodeUrl(String url, String encoding) {
        boolean hasPrefix = false;
        boolean hasSuffix = false;
        if (url == null || url.length() < 1) {
            return url;
        }
        // 检测后缀前缀是否是“/”
        if (url.charAt(0) == JOINER.charAt(0)) {
            hasPrefix = true;
        }
        if (url.charAt(url.length() - 1) == JOINER.charAt(0)) {
            hasSuffix = true;
        }
        // 通过"/"切片后，将切后地址分别转义，避免分隔符"/"也被转义的问题
        String[] address = split(url, false);
        try {
            for (int i = 0; i < address.length; i++) {
                System.out.println("转义前：" + address[i]);
                address[i] = URLEncoder.encode(address[i], encoding);
                System.out.println("转义后：" + address[i]);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Url编码异常");
        }
        return splice(address, hasPrefix, hasSuffix);
    }

    /**
     * 将字符用指定编码转义，区别于encodeUrl方法，本方法将传入字符串看过整体，不会区分里面"/"等特殊分隔符，也就说整个字符串都会被转义
     * 为保证转换后的url正确性，推荐使用本方法将url一段一段转义完成
     * @param str 被转义字符
     * @param encoding 编码
     * @return 转义后的字符
     */
    public static String encode(String str, String encoding) {
        try {
            return URLEncoder.encode(str, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("urlUtil转义异常，请检查给定编码是否正确");
        }
    }

    /**
     * 默认使用utf8编码转义
     * @param str 待转义字符
     * @return 转义后字符
     */
    public static String encode(String str){
        return encode(str, StandardCharsets.UTF_8.name());
    }


}
