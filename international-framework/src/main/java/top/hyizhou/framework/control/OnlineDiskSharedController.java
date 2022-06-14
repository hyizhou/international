package top.hyizhou.framework.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hyizhou.framework.config.constant.RespCode;
import top.hyizhou.framework.control.body.OnlineDiskToShareBody;
import top.hyizhou.framework.entity.Resp;
import top.hyizhou.framework.entity.User;
import top.hyizhou.framework.except.OnLineDiskException;
import top.hyizhou.framework.service.OnLineDiskService;

/**
 * 共享网盘文件的接口
 * 进行共享、取消共享、获取共享记录、查看共享文件，获取共享文件
 * @author huanggc
 * @date 2022/4/8 10:04
 */
@RestController
@RequestMapping("/shared")
public class OnlineDiskSharedController {
    private final Logger log = LoggerFactory.getLogger(OnlineDiskSharedController.class);
    private OnLineDiskService service;

    /**
     * 进行共享操作
     * @param user 用户
     * @param body 共享信息，包含共享文件、共享时间、验证码
     * @return 返回共享成功后的信息，包括查看链接，
     */
    @PostMapping("/toShare")
    public Resp<?> toShare(User user, OnlineDiskToShareBody body){
        // TODO 共享验证码未存储
        try {
            service.shareFile(user, body.getPath(), body.getTime());
            return null;
//            service.getSharedDetail()
        } catch (OnLineDiskException e) {
            log.error("文件共享失败：",e);
            return new Resp<>(RespCode.ERROR, "系统异常", null);
        }
    }
}
