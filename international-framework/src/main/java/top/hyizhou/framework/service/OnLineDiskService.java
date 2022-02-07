package top.hyizhou.framework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hyizhou.framework.mapper.SharedMapper;

/**
 * 网盘服务
 * @author hyizhou
 * @date 2022/2/1
 */
@Service
public class OnLineDiskService {
    private final Logger log = LoggerFactory.getLogger(OnLineDiskService.class);

    @Autowired
    private SharedMapper sharedMapper;
    @Autowired
    private OnLineDiskService onLineDiskService;

    /**
     * 初始化工作，用户创建网盘时调用的方法，如创建网盘信息表记录，创建物理盘中文件目录
     */
    public void create(){
        log.debug("用户网盘初始化开始");

        log.debug("用户网盘初始化结束");
    }

    /**
     * 注销网盘
     */
    public void delete(){

    }

    /**
     * 存储文件
     */
    public void save(){

    }

    /**
     * 读取文件
     */
    public void read(){}

    /**
     * 分享文件
     */
    public void shared(){}
}
