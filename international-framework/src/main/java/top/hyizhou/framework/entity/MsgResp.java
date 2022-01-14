package top.hyizhou.framework.entity;

/**
 * 只有一条属性用来存储信息，用于响应前端
 * @author hyizhou
 * @date 2022/1/14 9:33
 */
public class MsgResp {
    /** 提示信息 */
    private String msg;

    public MsgResp(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
