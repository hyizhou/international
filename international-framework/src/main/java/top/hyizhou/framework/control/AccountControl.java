package top.hyizhou.framework.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import top.hyizhou.framework.config.constant.RespCode;
import top.hyizhou.framework.entity.MsgResp;
import top.hyizhou.framework.entity.Resp;
import top.hyizhou.framework.entity.User;
import top.hyizhou.framework.service.AccountService;

/**
 * 提供账户相关接口
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
     * 获取登录用户详细信息
     * @param authentication spring security认证信息
     */
    @GetMapping("/details")
    @ResponseBody
    public Resp<?> details(Authentication authentication){
        String accountName = authentication.getName();
        User user = accountService.findUser(null, accountName);
        user.setPassword(null);
        return new Resp<>(RespCode.OK, null, user);
    }

    /**
     * 修改用户信息，依靠accountName作为唯一标识识别用户信息
     *
     * @param user 用户信息
     */
    @PostMapping("/modify")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> modify( @RequestBody User user) {
        if (log.isDebugEnabled()) {
            log.debug("修改信息：{}", user.toString());
        }
        String errMsg = accountService.modify(user);
        if (errMsg == null) {
            return ResponseEntity.status(HttpStatus.OK).body(new Resp<>(RespCode.OK, "修改成功", null));
        } else {
            return ResponseEntity.status(400).body(new Resp<>(RespCode.SERVER_ERROR, errMsg, null));
        }
    }

    /**
     * 修改账户名的接口
     */
    @PutMapping("/modifyAcName")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<MsgResp> modifyAccountName(@ModelAttribute("newAcName") String newName, Authentication authentication) {
        String oldName = authentication.getName();
        if (log.isDebugEnabled()){
            log.debug("账户名修改：{} -> {}", oldName, newName);
        }
        User user = accountService.findUser(null, oldName);
        String errMsg = accountService.modifyAccountName(oldName, newName);
        if (errMsg != null) {
            return ResponseEntity.status(400).body(new MsgResp(errMsg));
        }
        user = accountService.findUser(user.getId(), null);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getAccountName(), authentication.getCredentials(), authentication.getAuthorities()));
        log.info("修改账户名成功");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 修改用户名
     * @param userDetails spring security中用户信息
     * @param newUserName 新用户名
     */
    @PutMapping("/modifyUserName")
    @ResponseBody
    public Resp<?> modifyUserName(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("newUserName") String newUserName){
        String accountName = userDetails.getUsername();
        User user = accountService.findUser(null, accountName);
        user.setName(newUserName);
        String msg = accountService.modify(user);
        if (msg == null){
            return new Resp<>(RespCode.OK, null, null);
        }else {
            log.error(">> 修改用户名失败，错误原因：{}", msg);
            return new Resp<>(RespCode.SERVER_ERROR, msg, null);
        }
    }

    /**
     * 注销账户
     * @return 响应值
     */
    public MsgResp cancelAccount(User user){
        if (accountService.deleteAccount(user.getId())) {
            return new MsgResp("注销成功");
        }
        return new MsgResp("注销账号失败");
    }

}
