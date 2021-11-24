package top.hyizhou.framework.speech;

import com.microsoft.cognitiveservices.speech.SpeechConfig;

/**
 * 语音
 * @author huanggc
 * @date 2021/11/22 16:09
 */
public class CreateConfig {
    public void init(){
        SpeechConfig.fromAuthorizationToken("", "");
    }
}
