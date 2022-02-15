package top.hyizhou.framework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hyizhou.framework.entity.User;
import top.hyizhou.framework.mapper.OnLineDiskMapper;
import top.hyizhou.framework.mapper.SharedMapper;
import top.hyizhou.framework.pojo.OnLineDisk;

/**
 * 网盘服务
 * @author hyizhou
 * @date 2022/2/1
 */
@Service
public class OnLineDiskService {
    private final Logger log = LoggerFactory.getLogger(OnLineDiskService.class);

    /** shared（共享表）表mapper */
    @Autowired
    private SharedMapper sharedMapper;
    /** OnLineDisk（网盘信息表）表mapper */
    @Autowired
    private OnLineDiskMapper onLineDiskMapper;
    /** 实体目录路径 */
    private String onLineDiskDir;

    /**
     * 初始化工作，用户创建网盘时调用的方法，如创建网盘信息表记录，创建物理盘中文件目录
     * @param user 用户信息
     */
    public void create(User user){
        log.debug("用户网盘初始化开始");
        Integer userId = user.getId();
        // 验证用户是否以存在网盘
        OnLineDisk onLineDiskInfo = onLineDiskMapper.findById(userId);
        if (onLineDiskInfo != null){
            log.info("用户网盘初始化时发现以存在网盘信息");
            return;
        }
        // todo
        // 判断空间是否充足
        // 新建用户网盘目录
        // 信息记入表
        log.debug("用户网盘初始化结束");
    }

    /**
     * todo 注销网盘
     */
    public void delete(){

    }

    /**
     * todo 存储文件
     */
    public void save(){

    }

    /**
     * todo 读取文件
     */
    public void read(){}

    /**
     * todo 分享文件
     */
    public void shared(){}
}
