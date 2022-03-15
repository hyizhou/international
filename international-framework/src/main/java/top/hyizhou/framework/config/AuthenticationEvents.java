package top.hyizhou.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * 身份验证事件处理类
 * @author hyizhou
 * @date 2022/3/15 9:58
 */
@Component
public class AuthenticationEvents {
    private final static Logger logger = LoggerFactory.getLogger(AuthenticationEvents.class);
    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        logger.debug("用户认证成功：{}", success.getAuthentication().getName());
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failures){
        logger.debug("用户认证失败：{}", failures.getAuthentication().getName());
        logger.debug("具体异常类型：[{}]，异常信息：[{}]", failures.getException().getClass().getSimpleName(), failures.getException().getMessage());
    }

}
