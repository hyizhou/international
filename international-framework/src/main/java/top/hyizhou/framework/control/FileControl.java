package top.hyizhou.framework.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.hyizhou.framework.entity.Resp;
import top.hyizhou.framework.entity.SimpleFileInfo;
import top.hyizhou.framework.service.FileService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.List;

/**
 * 文件下载与目录展示接口
 * @author huanggc
 * @date 2021/11/8 15:26
 */
@RestController
@RequestMapping()
@CrossOrigin(origins = "*")
public class FileControl {
    static private final String DIR_URL = "/dir";
    private final Logger log = LoggerFactory.getLogger(FileControl.class);
    /** 路径匹配器 */
    private final AntPathMatcher pathMatcher;

    private final FileService service;

    public FileControl(AntPathMatcher pathMatcher, FileService service) {
        this.pathMatcher = pathMatcher;
        this.service = service;
    }


    /**
     * 对文件下载任务进行处理
     */
    private void doDown(HttpServletResponse response, String dirId, String path) throws IOException {
        try(OutputStream out = response.getOutputStream()) {
            // 输入流写入文件
            String fileName = service.writeStream(out, dirId, path);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition","attachment;filename="+fileName);
        }
    }

    /**
     * 请求路径组成：/file/"id"/文件路径 <br/>
     * 1、id：所开放目录的id <br/>
     * 2、文件路径：id所指目录中文件或目录相对路径 <br/><br/>
     *
     * 完成两个功能:<br/>
     * 1、若请求路径是目录，则返回文件列表，如：localhost/1/work/video，将展示/work/video目录中的文件列表。<br/>
     * 2、若请求路径是文件，则进行文件下载，如：localhost/1/work/video/day1.mp4，将下载day1.mp4文件。<br/>
     * @param id 多个开放目录时指对应目录的id
     */
    @GetMapping(value = FileControl.DIR_URL+ "/{id}/**")
    public Resp<List<SimpleFileInfo>> fileList(@PathVariable("id")  String id, HttpServletResponse response, HttpServletRequest request) throws IOException {
        String uri = request.getRequestURI();
        // 中文解码
        uri = URLDecoder.decode(uri,"utf-8");
        String path = pathMatcher.extractPathWithinPattern(FileControl.DIR_URL + "/" + id + "/*", uri);
        log.info("请求目录id：{}，请求文件路径：{}",id , path);
        if (service.isFile(id, path)) {
            // 是文件执行下载
            log.info("执行文件下载");
            doDown(response, id, path);
            return null;
        }else {
            // 展开目录
            log.info("展开了文件列表");
            return service.getDirectoryList(id, path);
        }

    }

    /**
     * 文件上传
     * @return 若是成功状态，返回200
     */
    @PostMapping(value = "/upload")
    public Resp<String> upload(@RequestParam(value = "file") MultipartFile file){
        return service.upload(file);
    }

    @GetMapping(value = "/download/{id:.*}")
    public ResponseEntity<Resource> download(@PathVariable String id){
        Resource resource = service.download(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
