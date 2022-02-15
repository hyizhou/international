package top.hyizhou.framework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hyizhou.framework.entity.User;
import top.hyizhou.framework.mapper.OnLineDiskMapper;
import top.hyizhou.framework.mapper.SharedMapper;
import top.hyizhou.framework.pojo.OnLineDisk;
import top.hyizhou.framework.utils.DiskUtil;
import java.io.File;

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
    private String createPath;

    /**
     * 初始化工作，用户创建网盘时调用的方法，如创建网盘信息表记录，创建物理盘中文件目录
     * @param user 用户信息
     */
    public void create(User user){
        log.debug("用户网盘初始化开始");
        Integer userId = user.getId();
        String accountName = user.getAccountName();
        // 验证用户是否以存在网盘
        OnLineDisk onLineDiskInfo = onLineDiskMapper.findById(userId);
        if (onLineDiskInfo != null){
            log.info("用户网盘初始化时发现已存在网盘");
            return;
        }
        // 判断空间是否充足，大于1G
        if (!DiskUtil.hasEnoughSpace(new File(createPath), 1073741824)) {
            log.error("用户网盘初始化失败--空间不足");
            return;
        }
        // 新建用户网盘目录
        String userDir = createPath + File.separator + accountName;
        if (!new File(userDir).mkdirs()) {
            log.error("用户创建网盘初始化失败--创建目录失败");
        }
        // 信息记入表
        OnLineDisk onLineDisk = new OnLineDisk();
        onLineDisk.setUserId(userId);
        onLineDisk.setDirName(userDir);
        onLineDisk.setAllSize(DefaultConf.allSize);
        onLineDisk.setUseSize(0L);
        onLineDisk.setShutoff(false);
        onLineDiskMapper.insert(onLineDisk);
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

    /**
     * 写一些配置
     */
    private static class DefaultConf{
        /** 新建的网盘默认分配1G空间 */
        public static long allSize = 1073741824;
    }
}
