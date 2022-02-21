package top.hyizhou.framework.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hyizhou.framework.entity.Resp;
import top.hyizhou.framework.entity.SimpleFileInfo;
import top.hyizhou.framework.entity.User;
import top.hyizhou.framework.except.OnLineDiskException;
import top.hyizhou.framework.service.AccountService;
import top.hyizhou.framework.service.OnLineDiskService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * 云盘控制层
 * @author hyizhou
 * @date 2022/2/21 15:52
 */
@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class OnlineDiskControl {
    private final Logger log = LoggerFactory.getLogger(OnlineDiskControl.class);
    @Autowired
    private OnLineDiskService service;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AntPathMatcher matcher;

    /**
     * TODO 用来给前端测试数据的
     */
    @GetMapping("disk/folder/**")
    public Resp<List> getFolderDetail(HttpSession session, HttpServletRequest request){
        String uri = null;
        try {
            uri = URLDecoder.decode(request.getRequestURI(), "utf8");
        } catch (UnsupportedEncodingException e) {
            uri = URLDecoder.decode(request.getRequestURI());
        }
        System.out.println(uri);
        String path = matcher.extractPathWithinPattern("disk/folder/**", uri);
        System.out.println(path);
        System.out.println("session的id = "+session.getId());
        User user = accountService.findUser(21, null);
        List<SimpleFileInfo> list;
        try {
            list = service.getFolderSub(user, path);
        } catch (OnLineDiskException e) {
            log.error("获取文件列表失败了,看上面日志找错去吧");
            log.error("", e);
            return null;
        }
        return new Resp<>("100", "", list);

    }
}
