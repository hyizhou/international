package top.hyizhou.framework.control;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.hyizhou.framework.config.constant.RespCode;
import top.hyizhou.framework.control.body.OnlineDiskUpdateBody;
import top.hyizhou.framework.entity.OnlinediskFileDetail;
import top.hyizhou.framework.entity.Resp;
import top.hyizhou.framework.entity.SimpleFileInfo;
import top.hyizhou.framework.entity.User;
import top.hyizhou.framework.except.OnLineDiskException;
import top.hyizhou.framework.service.AccountService;
import top.hyizhou.framework.service.OnLineDiskService;
import top.hyizhou.framework.utils.UrlUtil;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 云盘控制层
 * @author hyizhou
 * @date 2022/2/21 15:52
 */
@RestController
@RequestMapping("/netDisk/")
public class OnlineDiskControl extends DiskControlBase{
    private static final String URI = "uri";
    private static final String USER = "user";
    private static final String PATH = "path";
    private final OnLineDiskService service;
    private final AccountService accountService;

    public OnlineDiskControl(AntPathMatcher matcher, AccountService accountService, OnLineDiskService service) {
        super(matcher);
        this.service = service;
        this.accountService = accountService;
    }

    /**
     * 用户提前获取到用户与操作路径
     */
    @ModelAttribute
    private void findUser(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest req, Model model){
        // 一般经过验证的不太可能是null，若是null则表示没有登录
        if (userDetails == null){
            throw new UsernameNotFoundException("没有用户登入");
        }
        String username = userDetails.getUsername();
        User user = accountService.findUser(null, username);
        if (user == null){
            throw new UsernameNotFoundException("没有此用户: "+ username);
        }
        model.addAttribute(USER, user);
        model.addAttribute(URI, req.getRequestURI());
        // 尝试在此获取path：本控制器中的uri结构都是/netDisk/具体方法/path...，因此可通过从第三层取后面值获取path
        Path path = Paths.get(req.getRequestURI());
        String pathStr = "";
        if (path.getNameCount() > 2){
            pathStr = UrlUtil.decode(path.subpath(2, path.getNameCount()).toString());
        }
        model.addAttribute(PATH, pathStr);

    }

    /**
     * 获取目录子文件列表，请求路径"/netDisk/folder/**"
     * @return 响应报文
     */
    @GetMapping("/folder/**")
    public Resp<List<?>> getFolderSubs(@ModelAttribute(USER) User user, @ModelAttribute(PATH) String path){
        try{
            // 肯定不为空
            assert user != null;
            List<SimpleFileInfo> subs = service.getFolderSub(user, path);
            return new Resp<>(RespCode.OK, null, subs);
        } catch (OnLineDiskException e) {
            log.error(">> 用户[{}]获取文件列表失败：{}", user.getName(), path);
            return new Resp<>(RespCode.ERROR, e.getMessage(), null);
        }
    }

    /**
     * 文件下载
     */
    @GetMapping("/download/**")
    public ResponseEntity<?> download(@ModelAttribute(USER) User user, @ModelAttribute(PATH) String path){
        try{
            assert user != null;
            Resource resource = service.getFile(user, path);
            if (log.isDebugEnabled()){
                log.debug(">> 文件下载接口：用户：[{}]，路径：[{}]，获取文件名：[{}]", user.getAccountName(), path, resource.getFilename());
            }
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ UrlUtil.encode(resource.getFilename()) + "\"")
                    .body(resource);
        } catch (OnLineDiskException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Resp<String>("404", e.getMessage(), null));
        }
    }


    /**
     * 文件上传接口
     * @param file 前端上传的文件，从post请求中form表单中取出文件
     */
    @PostMapping("/upload/**")
    public Resp<?> upload(@RequestParam(value = "file")MultipartFile file, @ModelAttribute(USER) User user, @ModelAttribute(PATH) String path){
        try{
            OnlinediskFileDetail detail = service.saveFile(user, file, path);
            return new Resp<>(RespCode.OK, null, detail);
        } catch (OnLineDiskException e) {
            return new Resp<>(RespCode.ERROR, e.getMessage(), null);
        }
    }

    /**
     * 进行更新操作，包括移动，删除，重命名，复制
     * @param user 用户对象
     * @param body 操作的原路径和目标路径
     */
    @PutMapping({"/update/**", "/update"})
    public Resp<?> update(@ModelAttribute(USER) User user, @RequestBody OnlineDiskUpdateBody body){
        String oldPath = body.getOldPath();
        String targetPath = body.getTargetPath();
        try{
            switch (body.getType()){
                // 重命名
                case 1:
                    service.rename(user, oldPath, Paths.get(targetPath).getFileName().toString());
                    break;
                case 2:
                    // 移动
                    service.move(user, oldPath, targetPath);
                    break;
                case 3:
                    // 复制
                    service.copy(user, oldPath, targetPath);
                    break;
                default:
                    return new Resp<>(RespCode.ERROR, "更新类型码错误，应为{1(重命名),2(移动),3(复制)}", null);
            }
            return new Resp<>(RespCode.OK, null, null);
        } catch (OnLineDiskException e) {
            return new Resp<>(RespCode.ERROR, e.getMessage(), null);
        }
    }

}
