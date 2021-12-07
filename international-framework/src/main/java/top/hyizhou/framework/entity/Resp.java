package top.hyizhou.framework.entity;

/**
 * 基础响应报文
 * @author huanggc
 * @date 2021/12/7 9:58
 */
public class Resp<E> {
    private String code;
    private String msg;
    /** 更复杂的信息通过此属性发送 */
    private E body;

    public Resp(String code, String msg, E body) {
        this.code = code;
        this.msg = msg;
        this.body = body;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public E getBody() {
        return body;
    }

    public void setBody(E body) {
        this.body = body;
    }

    /**
     * 失败响应
     * @param code 错误码
     * @param msg 错误提示
     * @param <E> 为了保持与不同代码的兼容性，添加的泛型，在此方法中并没有实际用处
     * @return 本类实体对象
     */
    public static <E> Resp<E> failed(String code, String msg){
        return new Resp<E>(code, msg, null);
    }

    /**
     * 成功响应
     * @param body 消息体
     * @param <E> 消息体对象
     * @return 本类实体对象
     */
    public static <E> Resp<E> success(E body){
        return new Resp<>("200", "", body);
    }
}
