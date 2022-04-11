package top.hyizhou.framework.config.constant;

/**
 * 响应对象的响应码
 * @author hyizhou
 * @date 2022/2/25 11:25
 */
public class RespCode {
    public static final String OK = "200";
    public static final String BAD_REQUEST = "400";
    /** 账号已存在 */
    public static final String ACCOUNT_ALREADY_EXISTS = "401";
    /** 密码不合规 */
    public static final String PASSWORD_VIOLATION = "402";
    public static final String SERVER_ERROR = "500";
    /** 纯粹表示产生异常 */
    public static final String ERROR = "999";
}
