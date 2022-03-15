package top.hyizhou.framework.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hyizhou.framework.config.constant.RespCode;
import top.hyizhou.framework.entity.Resp;
import top.hyizhou.framework.entity.SimpleFileInfo;
import top.hyizhou.framework.entity.User;
import top.hyizhou.framework.except.OnLineDiskException;
import top.hyizhou.framework.service.AccountService;
import top.hyizhou.framework.service.OnLineDiskService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 云盘控制层
 * @author hyizhou
 * @date 2022/2/21 15:52
 */
@RestController
@RequestMapping("/netDisk/")
public class OnlineDiskControl extends DiskControlBase{
    @Autowired
    private OnLineDiskService service;
    @Autowired
    private AccountService accountService;

    public OnlineDiskControl(AntPathMatcher matcher, AccountService accountService, OnLineDiskService service) {
        super(matcher);
        this.service = service;
        this.accountService = accountService;
    }

    @ModelAttribute
    private User findUser(@AuthenticationPrincipal UserDetails userDetails){
        // 一般经过验证的不太可能是null，若是null则表示没有登录
        if (userDetails == null){
            throw new UsernameNotFoundException("没有用户登入");
        }
        String username = userDetails.getUsername();
        User user = accountService.findUser(null, username);
        if (user == null){
            throw new UsernameNotFoundException("没有此用户: "+ username);
        }
        return user;
    }

    /**
     * 获取目录子文件列表，请求路径"/netDisk/folder/**"
     * @param req 请求
     * @param model 装user对象
     * @return 响应报文
     */
    @GetMapping("/folder/**")
    public Resp<List<?>> getFolderSubs(HttpServletRequest req, Model model){
        String path = extractPath("/netDisk/folder/**", req.getRequestURI());
        User user = (User) model.getAttribute("user");
        try{
            assert user != null;
            List<SimpleFileInfo> subs = service.getFolderSub(user, path);
            return new Resp<>(RespCode.OK, null, subs);
        } catch (OnLineDiskException e) {
            log.error(">> 用户[{}]获取文件列表失败：{}", user.getName(), path);
            return new Resp<>(RespCode.ERROR, e.getMessage(), null);
        }
    }

}
