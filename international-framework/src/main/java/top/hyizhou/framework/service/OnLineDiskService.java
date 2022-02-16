package top.hyizhou.framework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import top.hyizhou.framework.config.property.BaseProperty;
import top.hyizhou.framework.entity.SimpleFileInfo;
import top.hyizhou.framework.entity.User;
import top.hyizhou.framework.except.OnLineDiskException;
import top.hyizhou.framework.mapper.OnLineDiskMapper;
import top.hyizhou.framework.mapper.SharedMapper;
import top.hyizhou.framework.pojo.OnLineDisk;
import top.hyizhou.framework.utils.DiskUtil;
import top.hyizhou.framework.utils.FilesUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

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
     * 存储文件
     * @param path 存储文件位置，是前端获得的虚拟位置并不是实际物理位置
     * @return true为成功，false则表示失败，若未抛出异常，则返回前端失败原因默认为“系统异常”
     */
    public boolean save(User user, MultipartFile file, String path) throws OnLineDiskException {
        // 判断上传的文件是否为空
        if (null == file || file.isEmpty()){
            log.error("云盘文件存储完成 -- 但文件为空");
            return true;
        }
        // todo 判断path是否标准

        // 获取剩余空间大小
        OnLineDisk userDisk = onLineDiskMapper.findById(user.getId());
        if (userDisk == null){
            log.error("云盘文件存储失败 -- 用户未开通云盘");
            throw new OnLineDiskException("未开通云盘");
        }
        // 拼接存储路径
        path = userDisk.getDirName() + File.separator + path;
        long freeSize = userDisk.getAllSize() - userDisk.getUseSize();
        // 获取文件判断文件是否过大
        if (file.getSize() > freeSize) {
            log.error("云盘文件存储失败 -- 空间不足，文件大小：[{}]，剩余空间:[{}]", file.getSize(), freeSize);
            throw new OnLineDiskException("文件过大");
        }
        // 将文件写入仓库
        try {
            file.transferTo(new File(path));
        } catch (IOException e) {
            log.error("云盘文件存储失败 -- 文件写入异常");
            log.error("", e);
            return false;
        }
        return true;
    }

    /**
     * 读取单文件
     * @param user 用户信息
     * @param file 文件路径
     * @return 资源对象，继承是输入流，方便将文件返回给前端
     */
    public Resource read(User user, String file) throws OnLineDiskException {
        // 判断用户是否存在
        OnLineDisk onLineDisk = onLineDiskMapper.findById(user.getId());
        if (null == onLineDisk){
            log.error("用户未开通云盘：{}", user);
            throw new OnLineDiskException("用户未开通云盘");
        }
        file = onLineDisk.getDirName()+ File.separator + file;
        if (!new File(file).exists()) {
            log.error("读取文件失败 -- 文件路径不存在：file={}", file);
            throw new OnLineDiskException("文件路径不存在");
        }
        // 返回资源对象
        return new PathResource(file);
    }

    /**
     * 读取目录
     */
    public List<SimpleFileInfo> list(User user, String path) throws OnLineDiskException {
        // 判断用户是否存在
        OnLineDisk onLineDisk = onLineDiskMapper.findById(user.getId());
        if (null == onLineDisk){
            log.error("用户未开通云盘：{}", user);
            throw new OnLineDiskException("用户未开通云盘");
        }
        path = onLineDisk.getDirName()+ File.separator + path;
        // todo 判断path正确性
        File dir = new File(path);
        // 判断是否存在
        if (!dir.exists()) {
            log.error("目录读取 -- 读取的目录不存在：path={}", path);
            throw new OnLineDiskException("读取目录不存在");
        }
        if (!dir.isDirectory()){
            log.error("目录读取 -- 读取路径并不是目录：path={}", path);
            throw new OnLineDiskException("读取目录不存在");
        }
        return SimpleFileInfo.createSimpleFileInfo(dir.listFiles());
    }

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
