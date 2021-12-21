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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.hyizhou.framework.service.FileService;
import top.hyizhou.framework.utils.UrlUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;

/**
 * 文件下载与目录展示接口
 * bug: url中存在[]方括号这种情况会tomcat会404，请求都不会到springmvc里面
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
            log.info("下载文件：{}",fileName);
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
    @GetMapping(value = "dir/{id}/**")
    public ModelAndView fileList(@PathVariable("id")  String id, HttpServletResponse response, HttpServletRequest request) throws IOException {
        String uri = request.getRequestURI();
        String primevalUri = uri;
        // 中文解码
        uri = URLDecoder.decode(uri,"utf-8");
        String path = pathMatcher.extractPathWithinPattern(FileControl.DIR_URL + "/" + id + "/*", uri);
        log.info("原始请求路径：{}", uri);
        if (service.isFile(id, path)) {
            // 是文件执行下载
            doDown(response, id, path);
            return null;
        }else {
            // 展开目录
            uri = primevalUri;
            uri = UrlUtil.formatSuffix(uri);
            uri = UrlUtil.format(uri);
            ModelAndView view = new ModelAndView("dir");
            // 目录中文件列表
            view.addObject("subFileList", service.getDirectoryList(id, path));
            // 当前目录路径
            view.addObject("dirPath", "/"+path);
            // 当前url路径，用于拼接为众多子路径
            view.addObject("urlPath", uri);
            // 当前url父路径，用于返回上一级
            view.addObject("parentUrl", UrlUtil.parentUrl(uri));
            return view;
        }

    }

    /**
     * 文件上传
     * TODO 判断文件hash值，已经上传的文件就不要重复存储了
     * @return 若是成功状态，则重定向到文件详情页面
     */
    @PostMapping(value = "/upload")
    public ModelAndView upload(@RequestParam(value = "file") MultipartFile file, RedirectAttributes model){
        String[] infos = service.upload(file).getBody();

        ModelAndView view = new ModelAndView("redirect:upload/detail/"+infos[0]);
//        view.addObject("isJustNow", true);
//        view.addObject("info", infos);
        model.addFlashAttribute("isJustNow", true);
        model.addFlashAttribute("info", infos);
        return view;
    }

    /**
     * 上传文件后，使用本接口进行文件获取
     * @param id 文件id
     * @return 文件资源
     */
    @GetMapping(value = "/upload/get/{id:.*}")
    public ResponseEntity<Resource> download(@PathVariable String id){
        Resource resource = service.download(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                // 文件名需要解析一下
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + service.readName(resource.getFilename()) + "\"")
                .body(resource);
    }

    /**
     * 文件上传后的文件详情页面、
     * TODO 若id不存在的情况，应返回“不存在”的提示页面，而不是返回500错误
     * @param id 文件对应的id
     */
    @GetMapping(value = "/upload/detail/{id}")
    public ModelAndView uploadDetail(@ModelAttribute("isJustNow") Boolean isJustNow ,@ModelAttribute("info") String[] infos, @PathVariable("id") String id){
        System.out.println("文件详情页id："+id);
        ModelAndView view = new ModelAndView("upload/detail");
        return view;
    }

    /**
     * 上传目录所有文件的文件列表
     */
    @GetMapping(value = "/upload/ls")
    public ModelAndView uploadLs(ModelAndView view){
        view.setViewName("upload/list");
        return view;
    }
}
