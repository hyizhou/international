package top.hyizhou.framework.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import top.hyizhou.framework.config.constant.RespCode;
import top.hyizhou.framework.entity.Resp;
import top.hyizhou.framework.entity.User;
import top.hyizhou.framework.service.AccountService;
import top.hyizhou.framework.utils.StrUtil;

/**
 * 用户身份相关接口，如注册，（登录、注销由框架完成），忘记密码
 * @author huanggc
 * @date 2022/3/24 15:54
 */
@RestController
@RequestMapping
public class IdentityController {
    private final Logger log;
    private final AccountService accountService;

    public IdentityController(AccountService accountService) {
        this.log = LoggerFactory.getLogger(this.getClass());
        this.accountService = accountService;
    }

    /**
     * 注册接口，只提供最基本地创建账号能力，用户更多信息需要成功后补充
     * @param accountName 账号名称
     * @param password 密码
     */
    @PostMapping("/register")
    public Resp<?> register(@ModelAttribute("userName") String accountName, @ModelAttribute("password") String password){
        // TODO 应避免被注册接口被刷
        // 判断用户名是否重复
        User user = accountService.findUser(null, accountName);
        if (user != null){
            return new Resp<>(RespCode.ACCOUNT_ALREADY_EXISTS, "账号已存在", null);
        }
        // 判断密码是否合规
        if (!checkPassword(password)){
            return new Resp<>(RespCode.PASSWORD_VIOLATION, "密码不符合规范", null);
        }
        // 进行注册操作
        user = new User();
        user.setAccountName(accountName);
        // TODO 存储密码时应存储加密字符
        user.setPassword(password);
        user.setName("注册用户");
        accountService.register(user);
        if (log.isDebugEnabled()){
            log.debug(">> 已完成注册用户：[{}]", accountName);
        }
        return new Resp<>(RespCode.OK, null, null);
    }

    /**
     * 忘记密码请求接口用于获取验证码
     * @param email 邮件
     */
    @GetMapping("/forgot/getVerification")
    public Resp<?> getVerificationCode(String email){
        return new Resp<>(RespCode.SERVER_ERROR, "接口还未开发完成", null);
    }

    /**
     * 忘记密码后此接口用于验证验证码
     */
    @PostMapping("/forgot/checkVerification")
    public Resp<?> checkVerificationCode(String email, String code){
        return new Resp<>(RespCode.SERVER_ERROR, "接口还未开发完成", null);
    }

    /**
     * 检查密码是否合规
     * @return 合规返回true
     */
    private boolean checkPassword(String password){
        // 判断空
        if (StrUtil.isEmpty(password)){
            return false;
        }
        // 判断长度
        if (password.length() < 4 || password.length() > 16){
            return false;
        }
        // 判断字符
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            // 判断是否为数字
            if ( c >= 48 && c <= 57){
                continue;
            }
            // 判断是否为大写字母
            if (c >= 65 && c <= 90){
                continue;
            }
            // 判断是否为小写字母
            if (c >= 97 && c <= 122){
                continue;
            }
            // 判断是否是下划线
            if (c == '_'){
                continue;
            }
            return false;
        }
        return true;
    }
}
