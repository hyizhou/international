package top.hyizhou.framework.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.hyizhou.framework.config.constant.RespCode;
import top.hyizhou.framework.entity.OnlinediskFileDetail;
import top.hyizhou.framework.entity.Resp;
import top.hyizhou.framework.entity.SimpleFileInfo;
import top.hyizhou.framework.entity.User;
import top.hyizhou.framework.except.OnLineDiskException;
import top.hyizhou.framework.service.AccountService;
import top.hyizhou.framework.service.OnLineDiskService;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 公共仓库接口
 * @author hyizhou
 * @date 2022/2/24 14:32
 */
@Lazy
@RestController
@CrossOrigin("*")
@RequestMapping("/api/public")
public class PublicOnlineDiskControl {
    private final Logger log = LoggerFactory.getLogger(PublicOnlineDiskControl.class);
    private final OnLineDiskService service;
    private final AntPathMatcher matcher;

    /** 公共用户，事先指定 */
    private final User publicUser;

    public PublicOnlineDiskControl(AccountService accountService, OnLineDiskService service, AntPathMatcher matcher) {
        this.service = service;
        this.matcher = matcher;
        this.publicUser = accountService.findUser(null, "public");
    }



    /**
     * 返回所给路径目录内部文件列表信息
     */
    @GetMapping("/folder/**")
    public Resp<List<?>> getFolderSubs(HttpServletRequest req){
        String path = extractPath("/api/public/folder/**", req.getRequestURI());
        try {
            List<SimpleFileInfo> subs = service.getFolderSub(publicUser, path);
            return new Resp<>(RespCode.OK, null, subs);
        } catch (OnLineDiskException e) {
            return new Resp<>(RespCode.ERROR, e.getMessage(), null);
        }
    }

    /**
     * 获得文件详细信息，此接口可以得到文件或目录详细信息
     */
    @GetMapping("/fileDetails/**")
    public Resp<OnlinediskFileDetail> getFileDetail(HttpServletRequest req){
        String path = extractPath("/api/public/fileDetails/**", req.getRequestURI());
        try {
            OnlinediskFileDetail detail = service.getFileDetail(publicUser, path);
            return new Resp<>(RespCode.OK, null, detail);
        } catch (OnLineDiskException e) {
            return new Resp<>(RespCode.ERROR, e.getMessage(), null);
        }
    }

    /**
     * 下载文件
     */
    @GetMapping("/download/**")
    public ResponseEntity<?> download(HttpServletRequest req){
        String path = extractPath("/api/public/download/**", req.getRequestURI());
        try {
            Resource resource = service.getFile(publicUser, path);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ URLEncoder.encode(resource.getFilename())+"\"")
                    .body(resource);
        } catch (OnLineDiskException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Resp<String>("404", e.getMessage(), null));
        }
    }

    @PostMapping("/upload/**")
    public  Resp<?> upload(@RequestParam(value = "file") MultipartFile file, HttpServletRequest req){
        String path = extractPath("/api/public/upload/**", req.getRequestURI());
        try {
            OnlinediskFileDetail detail = service.saveFile(publicUser, file, path);
            return new Resp<>(RespCode.OK, null, detail);
        } catch (OnLineDiskException e) {
            return new Resp<>(RespCode.ERROR, e.getMessage(), null);
        }
    }

    /**
     * 创建目录的接口
      */
    @PostMapping("/mkdir/**")
    public Resp<?> mkdir(HttpServletRequest req){
        String pathStr = extractPath("/api/public/mkdir/**", req.getRequestURI());
        try {
            service.mkdirOrFile(publicUser, pathStr, true);
            return new Resp<>(RespCode.OK, null, null);
        } catch (OnLineDiskException e) {
            return new Resp<>(RespCode.ERROR, e.getMessage(), null);
        }
    }

    /**
     * 重命名文件接口，接收json，其中包括旧文件名和新文件名。uri路径中指定操作目录。
     */
    @PutMapping("/update/**")
    public Resp<?> rename(@RequestBody Rename rename , HttpServletRequest req){
        String path = extractPath("/api/public/update/**", req.getRequestURI());
        try {
            service.rename(publicUser, Paths.get(path, rename.getOldName()).toString(), rename.getNewName());
            return new Resp<>(RespCode.OK, null, null);
        } catch (OnLineDiskException e) {
            return new Resp<>(RespCode.ERROR, e.getMessage(), null);
        }
    }

    public static final class Rename{
        private String oldName;
        private String newName;

        public String getOldName() {
            return oldName;
        }

        public void setOldName(String oldName) {
            this.oldName = oldName;
        }

        public String getNewName() {
            return newName;
        }

        public void setNewName(String newName) {
            this.newName = newName;
        }
    }

    /**
     * 获得uri上的资源路径
     * @param pattern 如何获取路径的表达式
     * @param uri uri路径，不包括请求协议
     * @return 从uri解析到的路径
     */
    private String extractPath(String pattern, String uri){
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
