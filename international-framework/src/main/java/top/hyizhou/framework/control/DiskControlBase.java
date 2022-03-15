package top.hyizhou.framework.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * @author hyizhou
 * @date 2022/3/15 15:49
 */
public abstract class DiskControlBase {
    protected final Logger log;
    private final AntPathMatcher matcher;

    public DiskControlBase(AntPathMatcher matcher) {
        this.log = LoggerFactory.getLogger(this.getClass());
        this.matcher = matcher;
    }

    /**
     * 获得uri上的资源路径
     * @param pattern 如何获取路径的表达式
     * @param uri uri路径，不包括请求协议
     * @return 从uri解析到的路径
     */
    protected String extractPath(String pattern, String uri){
        String path = "";
        try {
            String decodeUri = URLDecoder.decode(uri, StandardCharsets.UTF_8.toString());
            path = matcher.extractPathWithinPattern(pattern, decodeUri);
            if (log.isDebugEnabled()){
                log.debug("解析文件路径： path={}", path);
            }
        } catch (UnsupportedEncodingException ignored) {
            log.error("解码uri出错，检查一下客户端url编码是不是utf8");
        }
        return path;
    }
}
