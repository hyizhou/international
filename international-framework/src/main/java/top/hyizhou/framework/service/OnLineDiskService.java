package top.hyizhou.framework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import top.hyizhou.framework.config.property.BaseProperty;
import top.hyizhou.framework.entity.User;
import top.hyizhou.framework.mapper.OnLineDiskMapper;
import top.hyizhou.framework.mapper.SharedMapper;
import top.hyizhou.framework.pojo.OnLineDisk;
import top.hyizhou.framework.utils.DiskUtil;
import top.hyizhou.framework.utils.FilesUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 云盘服务 <br/>
 * 提供功能：初始化创建、注销、上传、分享、读取单个文件、读取目录、下载文件
 * @author hyizhou
 * @date 2022/2/1
 */
@Service
public class OnLineDiskService {
    private final Logger log = LoggerFactory.getLogger(OnLineDiskService.class);

    /** shared（共享表）表mapper */
    private final SharedMapper sharedMapper;
    /** OnLineDisk（网盘信息表）表mapper */
    private final OnLineDiskMapper onLineDiskMapper;
    /** 实体目录路径 */
    private final String warehouse;
    /** 仓库类型 */
    private final String storageType;

    public OnLineDiskService(SharedMapper sharedMapper, OnLineDiskMapper onLineDiskMapper, BaseProperty property) {
        this.sharedMapper = sharedMapper;
        this.onLineDiskMapper = onLineDiskMapper;
        this.warehouse = property.getOnlineDisk().getWarehouse();
        this.storageType = property.getOnlineDisk().getStorageType();
    }

    /**
     * 初始化工作，用户创建网盘时调用的方法，如创建网盘信息表记录，创建物理盘中文件目录
     * @param user 用户信息
     */
    public boolean create(User user){
        log.info("{} 用户网盘初始化开始", user.getAccountName());
        Integer userId = user.getId();
        String accountName = user.getAccountName();
        // 验证用户是否以存在网盘
        OnLineDisk onLineDiskInfo = onLineDiskMapper.findById(userId);
        if (onLineDiskInfo != null){
            log.info("用户网盘初始化时发现已存在网盘");
            return false;
        }
        // 判断空间是否充足，大于1G
        try {
            if (!DiskUtil.hasEnoughSpace(new File(warehouse), DefaultConf.allSize)) {
                log.error("用户网盘初始化失败--空间不足");
                return false;
            }
        } catch (FileNotFoundException e) {
            log.error("用户网盘初始化失败 -- 检查配置文件查看判断存储路径是否存在");
            log.error("", e);
            return false;
        }
        // 新建用户网盘目录
        String userDir = warehouse + File.separator + accountName;
        if (!new File(userDir).mkdirs()) {
            log.error("用户创建网盘初始化失败--创建目录失败");
            return false;
        }
        // 信息记入表
        OnLineDisk onLineDisk = new OnLineDisk();
        onLineDisk.setUserId(userId);
        onLineDisk.setDirName(userDir);
        onLineDisk.setAllSize(DefaultConf.allSize);
        onLineDisk.setUseSize(0L);
        onLineDisk.setShutoff(false);
        onLineDiskMapper.insert(onLineDisk);
        log.info("{} 用户网盘初始化结束", user.getAccountName());
        return true;
    }

    /**
     * 注销网盘，将表记录添加删除标志
     */
    public boolean delete(User user){
        Integer userId = user.getId();
        // 若不存在记录则不需要删除
        if (onLineDiskMapper.findById(userId) == null){
            return true;
        }
        // 将表中信息标记为删除
        OnLineDisk onLineDisk = new OnLineDisk();
        onLineDisk.setUserId(userId);
        onLineDisk.setShutoff(true);
        return onLineDiskMapper.update(onLineDisk) == 1;
    }


    /**
     * 清除，将删除物理磁盘数据
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean clear(User user){
        if (delete(user)) {
            // 删除用户云盘目录
            OnLineDisk onLineDisk = onLineDiskMapper.findById(user.getId());
            String dir = onLineDisk.getDirName();
            log.info("清除用户云盘目录--{}", dir);
            // 删除目录
            try {
                FilesUtil.rm(dir);
            } catch (IOException e) {
                log.error("清除用户云盘异常 -- 删除目录失败");
                log.error("", e);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
            return true;
        }
        // 表示数据库中存在用户云盘记录，但是标记为删除失败
        return false;
    }

    /**
     * todo 存储文件
     */
    public void save(){

    }

    /**
     * todo 读取单文件
     */
    public void read(){}

    /**
     * todo 读取目录
     */
    public void list(){}

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
