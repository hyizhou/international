package top.hyizhou.monitor.bean;

import java.util.List;

/**
 * 记录历史信息的接口
 *
 * @author hyizhou
 * @date 2021/10/13 10:29
 */
public interface History<T> {
    /**
     * 进行一次信息记录
     */
    void record();

    /**
     * 获取记录的历史信息表
     * @return 历史信息表，有先后顺序
     */
    List<T> getHistory();
}
