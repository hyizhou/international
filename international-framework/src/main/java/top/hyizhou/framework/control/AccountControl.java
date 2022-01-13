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
import top.hyizhou.framework.entity.User;
import top.hyizhou.framework.service.AccountService;

/**
 * 提供账户相关接口
 * TODO 对于此处的接口，需要拦截器验证其操作的是自己账户后才能放行
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
     * @param user 用户信息
     * @return 修改完成则返回true
     */
    @PostMapping("/modify")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<String> modify(@RequestBody User user){
        if (log.isDebugEnabled()){
            log.debug("修改信息：{}", user.toString());
        }
        String msg = accountService.modify(user);

        if (msg == null){
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }else {
            return ResponseEntity.status(400).body(msg);
        }
    }


}
