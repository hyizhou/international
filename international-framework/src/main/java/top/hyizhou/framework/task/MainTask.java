package top.hyizhou.framework.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.hyizhou.framework.utils.DateUtil;
import top.hyizhou.framework.utils.container.LoggedOnContainer;

/**
 * 自动任务类
 * @author hyizhou
 * @date 2022/1/26
 */
@Component
public class MainTask {
    private final Logger log = LoggerFactory.getLogger(MainTask.class);
    private final LoggedOnContainer logged;

    public MainTask(LoggedOnContainer logged) {
        this.logged = logged;
    }

    /**
     * 定时清理过期的登录用户。清理时间，每日零点。
     */
    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void loggedOnClear(){
        long startTime = System.currentTimeMillis();
        int clearLen = logged.clear();
        long endTime = System.currentTimeMillis();
        log.info("自动任务{} - 过期登录用户清理耗时:{}ms，清理条数：{}条", DateUtil.currentTimeReadable(), (endTime-startTime), clearLen);
    }
}
