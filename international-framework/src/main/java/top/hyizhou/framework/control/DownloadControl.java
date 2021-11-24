package top.hyizhou.framework.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import top.hyizhou.framework.service.DownloadService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 路径匹配测试
 * @author huanggc
 * @date 2021/11/8 15:26
 */
@RestController
@RequestMapping()
@CrossOrigin(origins = "*")
public class DownloadControl {
    /** 文件下载业务请求路径 */
    static private final String DOWNLOAD_URI = "/download";
    private final Logger log = LoggerFactory.getLogger(DownloadControl.class);
    /** 路径匹配器 */
    private final AntPathMatcher pathMatcher;

    private final DownloadService service;

    public DownloadControl(AntPathMatcher pathMatcher, DownloadService service) {
        this.pathMatcher = pathMatcher;
        this.service = service;
    }

    /**
     * 进行文件下载
     * 路径模式：xxx.com/download/目录/详细路径...
     * @param response 响应
     * @param request 请求
     */
    @GetMapping(value=DownloadControl.DOWNLOAD_URI +"/{dir}/**",produces = "application/json;charset=UTF-8")
    public void fileDownload(@PathVariable("dir") String dir, HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException {
        String uri = request.getRequestURI();
        // 带中文或空格需要使用此方法解码回来
        uri = URLDecoder.decode(uri,"utf-8");
        System.out.println(request.getCharacterEncoding());
        String filePath = pathMatcher.extractPathWithinPattern(DOWNLOAD_URI+"/"+dir+"/*", uri);
        log.info("下载路径：{}", filePath);
        log.info("下载目录id：{}", dir);

        try(OutputStream out = response.getOutputStream()) {
            // 输入流写入文件
            String fileName = service.pushFileStream(out, dir, filePath);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition","attachment;filename="+fileName);
        } catch (IOException e) {
            log.error("异常：",e);
        }
    }
}
