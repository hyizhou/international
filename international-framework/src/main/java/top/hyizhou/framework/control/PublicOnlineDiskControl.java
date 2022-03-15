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
import java.net.URLEncoder;
import java.nio.file.Paths;
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
public class PublicOnlineDiskControl extends DiskControlBase {
    private final Logger log = LoggerFactory.getLogger(PublicOnlineDiskControl.class);
    private final OnLineDiskService service;

    /** 公共用户，事先指定 */
    private final User publicUser;

    public PublicOnlineDiskControl(AccountService accountService, OnLineDiskService service, AntPathMatcher matcher) {
        super(matcher);
        this.service = service;
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
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ URLEncoder.encode(resource.getFilename(), "utf-8")+"\"")
                    .body(resource);
        } catch (OnLineDiskException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Resp<String>("404", e.getMessage(), null));
        } catch (UnsupportedEncodingException ignore) {
            log.error("encode指定编码错误，按理不应产生此错误");
            throw new RuntimeException();
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
     * 更新文件，可以执行重命名，移动，复制，由接收到的报文json数据决定
     */
    @PutMapping({"/update/**", "/update"})
    public Resp<?> update(@RequestBody UpdateMsg  updateMsg){
        String oldPath = updateMsg.oldPath;
        String targetPath = updateMsg.targetPath;
        try {
            switch (updateMsg.getType()) {
                // 重命名
                case 1:
                    service.rename(publicUser, oldPath, Paths.get(targetPath).getFileName().toString());
                    break;
                case 2:
                    // 移动
                    service.move(publicUser, oldPath, targetPath);
                    break;
                case 3:
                    // 复制
                    service.copy(publicUser, oldPath, targetPath);
                    break;
                default:
                    return new Resp<>(RespCode.ERROR, "更新类型码错误，应为{1,2,3}", null);
            }
            return new Resp<>(RespCode.OK, null, null);
        } catch (OnLineDiskException e) {
            return new Resp<>(RespCode.ERROR, e.getMessage(), null);
        }
    }

    /**
     * 如何进行更新的信息
     */
    public static final class UpdateMsg{
        private String oldPath;
        private String targetPath;
        /** 操作类型，1，重命名， 2，移动， 3，复制 */
        private int type;

        public String getOldPath() {
            return oldPath;
        }

        public void setOldPath(String oldPath) {
            this.oldPath = oldPath;
        }

        public String getTargetPath() {
            return targetPath;
        }

        public void setTargetPath(String targetPath) {
            this.targetPath = targetPath;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

}
