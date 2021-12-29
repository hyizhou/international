package top.hyizhou.framework.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * cookie工具类
 *
 * @author hyizhou
 * @date 2021/12/29 16:11
 */
public class CookieUtil {
    /**
     * 用以方便获取cookie的值value。
     * 将request对象中cookie的值转换成，name-value形式的Map
     *
     * @param req 请求
     * @return 若没有cookie，则返回空的map，否则正确返回map
     */
    public static Map<String, String> getCookieValueMap(HttpServletRequest req) {
        Map<String, String> map = new HashMap<>();
        getCookieMap(req).forEach((key, value) -> map.put(key, value.getValue()));
        return map;
    }

    /**
     * 将请求中的cookie转换成map，格式为name-cookie
     * @param req 请求
     * @return 若没有cookie则返回长度为0的map，否则正常返回
     */
    public static Map<String, Cookie> getCookieMap(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return new HashMap<>(0);
        }
        Map<String, Cookie> cookieMap = new HashMap<>(cookies.length);
        for (Cookie cookie : cookies) {
            cookieMap.put(cookie.getName(), cookie);
        }
        return cookieMap;
    }

}
