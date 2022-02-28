package top.hyizhou.framework.control.interceptor;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import top.hyizhou.framework.config.constant.RespCode;
import top.hyizhou.framework.entity.Resp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 检查路径中不允许出现的字符，拦截“/api/public/**”的请求
 * @author hyizhou
 * @date 2022/2/25 17:57
 */
public class BadPathInterceptor implements HandlerInterceptor {
    private final Logger log = LoggerFactory.getLogger(BadPathInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Path path = Paths.get(request.getRequestURI());
        for (int i = 0; i < path.getNameCount(); i++) {
            if ("..".equals(path.getName(i).toString())){
                log.error(">> 请求路径中出现非法字符：{}", request.getRequestURI());
                response.setContentType("application/json; charset=utf-8");
                response.getWriter().append(respBody("请求路径存在非法字符：'..'"));
                return false;
            }
        }
        return true;
    }

    private String respBody(String msg){
        Resp<String> resp = new Resp<>(RespCode.BAD_REQUEST, msg, null);
        return JSON.toJSONString(resp);
    }
}
