package top.hyizhou.framework.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.hyizhou.framework.entity.MsgResp;
import top.hyizhou.framework.entity.User;
import top.hyizhou.framework.service.AccountService;

import java.util.Map;

/**
 * 提供账户相关接口
 * TODO 对于此处的接口，需要拦截器验证其操作的是自己账户后才能放行，既然是自己账户，应该不需要在方法参数上传入用户相关信息了
 *
 * @author hyizhou
 * @date 2022/1/13 15:16
 */
@RequestMapping("/account")
@Controller
public class AccountControl {
    private final static Logger log = LoggerFactory.getLogger(AccountControl.class);
    private final AccountService accountService;

    public AccountControl(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 修改用户信息，依靠accountName作为唯一标识识别用户信息
     *
     * @param user 用户信息
     */
    @PostMapping("/modify")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<MsgResp> modify( @RequestBody User user) {
        if (log.isDebugEnabled()) {
            log.debug("修改信息：{}", user.toString());
        }
        String errMsg = accountService.modify(user);
        if (errMsg == null) {
            return ResponseEntity.status(HttpStatus.OK).body(new MsgResp(null));
        } else {
            return ResponseEntity.status(400).body(new MsgResp(errMsg));
        }
    }

    /**
     * 修改账户名的接口
     * @param map 存储旧名和新名
     */
    @PostMapping("/modifyAcName")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<MsgResp> modifyAccountName(@RequestBody Map<String, String> map) {

        String oldName = map.get("oldName");
        String newName = map.get("newName");
        if (log.isDebugEnabled()){
            log.debug("账户名修改：{} -> {}", oldName, newName);
        }
        String errMsg = accountService.modifyAccountName(oldName, newName);
        if (errMsg != null) {
            return ResponseEntity.status(400).body(new MsgResp(errMsg));
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 注销账户
     * @return 响应值
     */
    public MsgResp cancelAccount(User user){
        if (accountService.deleteAccount(user.getId())) {
            return new MsgResp(null);
        }
        return new MsgResp("注销账号失败");
    }


}
