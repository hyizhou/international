package top.hyizhou.framework.speech;

import com.microsoft.cognitiveservices.speech.SpeechConfig;

/**
 * 微软语音配置，需要申请注册微软相关账号
 * @author huanggc
 * @date 2021/11/22 16:09
 */
public class CreateConfig {
    public void init(){
        SpeechConfig.fromAuthorizationToken("", "");
    }
}