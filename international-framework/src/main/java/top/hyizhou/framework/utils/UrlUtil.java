package top.hyizhou.framework.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * URL工具类，使用时请不要带协议：如http：//localhost/a/ 只需要输入localhost/a/
 *
 * @author huanggc
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
     * @param url 资源定位符
     * @return 存储地址元素的数组
     */
    public static String[] split(String url) {
        String[] split = url.split("/");
        String[] reply = new String[split.length];
        int index = 0;
        for (String s : split) {
            if (!"".equals(s)) {
                reply[index++] = s;
            }
        }
        reply[index - 1] = reply[index - 1].split("\\?")[0];
        return Arrays.copyOfRange(reply, 0, index);
    }

    /**
     * 将切片后的地址，重新拼接为完整URL
     *
     * @param address   切片后地址数组
     * @param hasPrefix 是否带上前缀'/'
     * @return 完整地址
     */
    public static String splice(String[] address, boolean hasPrefix) {
        StringBuilder ss = new StringBuilder();
        if (hasPrefix) {
            ss.append(JOINER);
        }
        for (String a : address) {
            ss.append(a).append(JOINER);
        }
        return ss.toString();
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
        String[] split = split(url);
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

    public static String encode(String url) {
        System.out.println(url);
        String ur = encode(url, "utf-8");
        System.out.println("转义后：" + ur);
        return url;
    }

    public static String encode(String url, String encoding) {
        String enUrl;
        boolean hasPrefix = false;
        if (url.length() < 1) {
            return url;
        }
        if (url.charAt(0) == JOINER.charAt(0)) {
            hasPrefix = true;
        }
        String[] address = split(url);
        try {
            for (int i = 0; i < address.length; i++) {
                address[i] = URLEncoder.encode(address[i], encoding);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Url编码异常");
        }
        return splice(address, hasPrefix);

    }

}
