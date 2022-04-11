package top.hyizhou.framework.control;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.hyizhou.framework.control.body.OnlineDiskUpdateBody;
import top.hyizhou.framework.entity.OnlinediskFileDetail;
import top.hyizhou.framework.entity.Resp;
import top.hyizhou.framework.entity.User;
import top.hyizhou.framework.service.AccountService;
import top.hyizhou.framework.utils.UrlUtil;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 公共云盘空间的接口
 * @author huanggc
 * @date 2022/3/23 11:14
 */
@RestController
@Lazy
@RequestMapping("/publicDisk")
public class PublicOnlineDiskController {
    private final User publicUser;
    private final OnlineDiskControl control;
    private static final String PATH = "path";

    public PublicOnlineDiskController(AccountService accountService, OnlineDiskControl control) {
        this.publicUser = accountService.findUser(null, "public");
        this.control = control;
    }

    @ModelAttribute
    protected void findUser(HttpServletRequest req, Model model) {
        // uri从索引为2开始取出path
        int cutLen = 2;
        // 尝试在此获取path：本控制器中的uri结构都是/publicDisk/具体方法/path...，因此可通过从第三层取后面值获取path
        Path path = Paths.get(req.getRequestURI());
        String pathStr = "";
        if (path.getNameCount() > cutLen) {
            pathStr = UrlUtil.decode(path.subpath(2, path.getNameCount()).toString());
        }
        model.addAttribute(PATH, pathStr);
    }

    /**
     * 获取目录文件列表
     */
    @GetMapping("/folder/**")
    public Resp<List<?>> getFolderSubs(@ModelAttribute(PATH) String path) {
        return control.getFolderSubs(this.publicUser, path);
    }

    /**
     * 文件下载
     */
    @GetMapping("/download/**")
    public ResponseEntity<?> download(@ModelAttribute(PATH) String path) {
        return control.download(publicUser, path);
    }

    /**
     * 文件上传接口
     *
     * @param file 前端上传的文件，从post请求中form表单中取出文件
     */
    @PostMapping("/upload/**")
    public Resp<?> upload(@RequestParam(value = "file") MultipartFile file, @ModelAttribute(PATH) String path) {
        return control.upload(file, publicUser, path);
    }

    /**
     * 进行更新操作，包括移动，重命名，复制
     *
     * @param body 操作的原路径和目标路径
     */
    @PutMapping({"/update/**", "/update"})
    public Resp<?> update(@RequestBody OnlineDiskUpdateBody body) {
        return control.update(publicUser, body);
    }

    /**
     * 删除一个路径上的文件或目录
     */
    @DeleteMapping("/delete/**")
    public Resp<?> delete(@ModelAttribute(PATH) String path) {
        return control.delete(publicUser, path);
    }
    /**
     * 获取文件或目录详情
     */
    @GetMapping("/details/**")
    public Resp<OnlinediskFileDetail> getFileDetail(@ModelAttribute(PATH) String path){
        return control.getFileDetail(publicUser, path);
    }
}
