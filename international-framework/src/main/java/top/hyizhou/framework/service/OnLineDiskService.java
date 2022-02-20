package top.hyizhou.framework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import top.hyizhou.framework.config.property.BaseProperty;
import top.hyizhou.framework.entity.OnlinediskFileDetail;
import top.hyizhou.framework.entity.SharedFileDetail;
import top.hyizhou.framework.entity.SimpleFileInfo;
import top.hyizhou.framework.entity.User;
import top.hyizhou.framework.except.OnLineDiskException;
import top.hyizhou.framework.mapper.OnLineDiskMapper;
import top.hyizhou.framework.mapper.SharedMapper;
import top.hyizhou.framework.pojo.OnLineDisk;
import top.hyizhou.framework.pojo.SharedPojo;
import top.hyizhou.framework.utils.DateUtil;
import top.hyizhou.framework.utils.DiskUtil;
import top.hyizhou.framework.utils.FilesUtil;
import top.hyizhou.framework.utils.StrUtil;
import top.hyizhou.framework.utils.onlinedisk.LocalWarehouse;
import top.hyizhou.framework.utils.onlinedisk.Warehouse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 云盘服务 <br/>
 * 提供功能：
 *   - 创建云盘、注销云盘、恢复云盘、删除云盘
 *   - 基本操作
 *     + 增：创建目录，创建文件，上传文件
 *     + 删：删除目录/文件
 *     + 改：修改路径（可借此实现重命名），修改文件（待定）
 *     + 查：文件详情，文件列表、分享记录、文件内容（待定）
 *     + 复：恢复删除文件（待定）
 *   - 分享
 *     + 分享文件/目录、获取分享文件、查看分享文件/目录详情、取消分享
 * @author hyizhou
 * @date 2022/2/1
 */
@Service
public class OnLineDiskService {
    private final Logger log = LoggerFactory.getLogger(OnLineDiskService.class);

    /** shared（共享表）表mapper */
    private final SharedMapper sharedMapper;
    /** OnLineDisk（云盘信息表）表mapper */
    private final OnLineDiskMapper onLineDiskMapper;
    /** 实体目录路径 */
    private final String warehousePath;
    /** 仓库类型 */
    private final String storageType;
    private final Warehouse warehouse;

    public OnLineDiskService(SharedMapper sharedMapper, OnLineDiskMapper onLineDiskMapper, BaseProperty property) {
        this.sharedMapper = sharedMapper;
        this.onLineDiskMapper = onLineDiskMapper;
        this.warehousePath = property.getOnlineDisk().getWarehouse();
        this.storageType = property.getOnlineDisk().getStorageType();
        this.warehouse = new LocalWarehouse(warehousePath);
    }

    /**
     * 初始化工作，用户创建云盘时调用的方法，如创建云盘信息表记录，创建物理盘中文件目录
     * @param user 用户信息
     */
    public boolean create(User user){
        log.info("{} 用户云盘初始化开始", user.getAccountName());
        Integer userId = user.getId();
        String accountName = user.getAccountName();
        // 验证用户是否以存在云盘
        OnLineDisk onLineDiskInfo = onLineDiskMapper.findById(userId);
        if (onLineDiskInfo != null){
            log.info("用户云盘初始化时发现已存在云盘");
            return false;
        }
        // 判断空间是否充足，大于1G
        try {
            if (!DiskUtil.hasEnoughSpace(new File(warehousePath), DefaultConf.allSize)) {
                log.error("用户云盘初始化失败--空间不足");
                return false;
            }
        } catch (FileNotFoundException e) {
            log.error("用户云盘初始化失败 -- 检查配置文件查看判断存储路径是否存在");
            log.error("", e);
            return false;
        }
        // 新建用户云盘目录
        String userDir = warehousePath + FilesUtil.separator + accountName;
        if (!new File(userDir).mkdirs()) {
            log.error("用户创建云盘初始化失败--创建目录失败");
            return false;
        }
        log.info("云盘初始化 -- 创建仓库：{}",userDir);
        // 信息记入表
        OnLineDisk onLineDisk = new OnLineDisk();
        onLineDisk.setUserId(userId);
        onLineDisk.setDirName(userDir);
        onLineDisk.setAllSize(DefaultConf.allSize);
        onLineDisk.setUseSize(0L);
        onLineDisk.setShutoff(false);
        onLineDiskMapper.insert(onLineDisk);
        log.info("{} 用户云盘初始化结束", user.getAccountName());
        return true;
    }

    /**
     * 删除云盘，将表记录添加删除标志，可恢复
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
     * 删除的云盘恢复
     * @param user 用户信息
     * @return false表示已经无法恢复，true则表示不需要恢复或恢复成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean recover(User user){
        //先判断记录是否存在
        OnLineDisk userDisk = onLineDiskMapper.findById(user.getId());
        if (userDisk == null){
            log.error("用户云盘恢复 -- 用户未创建过或已彻底清除");
            return false;
        }
        // 标志设置为true才需要恢复，否则不用处理
        if (!userDisk.getShutoff()){
            return true;
        }
        userDisk.setShutoff(false);
        onLineDiskMapper.update(userDisk);
        return true;
    }


    /**
     * 清除，将不可恢复
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
            }catch (NoSuchFileException e) {
                log.info("清除用户云盘 -- 用户目录已不存在");
            }catch (IOException e) {
                log.error("清除用户云盘异常 -- 删除目录失败");
                log.error("", e);
                // 删除目录失败则回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
            // 最后彻底删除记录
            onLineDiskMapper.delete(user.getId());
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
    public boolean saveFile(User user, MultipartFile multipartFile, String path) throws OnLineDiskException {
        // 判断上传的文件是否为空
        if (null == multipartFile || multipartFile.isEmpty()){
            log.error("云盘文件存储完成 -- 但文件为空");
            return true;
        }
        // 获取剩余空间大小
        OnLineDisk userDisk = onLineDiskMapper.findById(user.getId());
        if (userDisk == null || userDisk.getShutoff()){
            log.error("云盘文件存储失败 -- 用户未开通云盘");
            throw new OnLineDiskException("未开通云盘");
        }
        // 拼接存储路径
        path = FilesUtil.join(userDisk.getDirName(), path);
        long freeSize = userDisk.getAllSize() - userDisk.getUseSize();
        // 获取文件判断文件是否过大
        if (multipartFile.getSize() > freeSize) {
            log.error("云盘文件存储失败 -- 空间不足，文件大小：[{}]，剩余空间:[{}]", multipartFile.getSize(), freeSize);
            throw new OnLineDiskException("文件过大");
        }
        // 将文件写入仓库
        try {
            File targetFile = new File(path);
            if (!targetFile.exists()) {
                if (!targetFile.mkdirs()) {
                    log.error("云盘文件存储失败 -- 文件目录创建异常：{}", targetFile.getAbsolutePath());
                    return false;
                }
            }
            multipartFile.transferTo(new File(FilesUtil.join(path, multipartFile.getName())));
        } catch (IOException e) {
            log.error("云盘文件存储失败 -- 文件写入异常");
            log.error("", e);
            return false;
        }
        // 增加已使用空间
        userDisk.setUseSize(userDisk.getUseSize() + multipartFile.getSize());
        onLineDiskMapper.update(userDisk);
        return true;
    }

    /**
     * 创建目录或文件
     * @param user 用户信息
     * @param path 创建路径
     * @param isDir 是否为目录
     */
    public void mkdirOrFile(User user, String path, boolean isDir) throws OnLineDiskException {
        OnLineDisk onLineDiskData = findOnLineDiskData(user);
        path = FilesUtil.join(new File(onLineDiskData.getDirName()).getName(), path);
        if (isDir){
            if (!warehouse.mkdir(path)) {
                log.error("云盘创建目录失败 -- 检查一下路径把：{}", path);
                throw new OnLineDiskException("请检查路径");
            }
        }else {
            // 创建一个空白文件
            if (!warehouse.save(path, null)){
                log.error("云盘创建文件失败 -- 请检查一下路径：{}", path);
                throw new OnLineDiskException("请检查路径");
            }
        }
    }

    /**
     * 获取单文件
     * @param user 用户信息
     * @param path 文件路径
     * @return 资源对象，继承是输入流，方便将文件返回给前端
     */
    public Resource getFile(User user, String path) throws OnLineDiskException {
        OnLineDisk onLineDiskData = findOnLineDiskData(user);
        path = FilesUtil.join(new File(onLineDiskData.getDirName()).getName(), path);
        Resource resource = warehouse.getFile(path);
        if (resource == null){
            log.error("获取文件资源失败 -- path=[{}]", path);
            throw new OnLineDiskException("资源不存在");
        }
        return  resource;
    }

    /**
     * 获取单个文件详细信息
     */
    public OnlinediskFileDetail getFileDetail(User user, String path) throws OnLineDiskException {
        OnLineDisk onLineDiskData = findOnLineDiskData(user);
        path = FilesUtil.join(new File(onLineDiskData.getDirName()).getName(), path);
        OnlinediskFileDetail detail = new OnlinediskFileDetail();
        // 获取文件基本信息
        SimpleFileInfo fileInfo = warehouse.getFileInfo(path);
        if (fileInfo == null){
            log.error("获取文件详细失败 -- 路径：{}", path);
            throw new OnLineDiskException("请检查路径");
        }
        detail.setDirectory(fileInfo.getIsDirectory());
        detail.setLength(fileInfo.getLength());
        detail.setName(fileInfo.getName());
        // 判断是否分享
        detail.setShear(isShare(path));
        return detail;
    }

    /**
     * 获取分享的文件详情
     * @param sharedPath 分享文件路径，如文件全路径为(D:/warehouse/userHouse/book/aa.txt)，本处应填(userHouse/book/aa.txt)
     * @return 文件详情对象
     */
    public SharedFileDetail getSharedDetail(String sharedPath) throws OnLineDiskException {
        SharedFileDetail detail = new SharedFileDetail();
        SimpleFileInfo fileInfo = warehouse.getFileInfo(sharedPath);
        if (fileInfo == null){
            log.error("获取分享文件详情失败 -- 路径：{}", sharedPath);
            throw new OnLineDiskException("请检查路径");
        }
        detail.setDirectory(fileInfo.getIsDirectory());
        detail.setLength(fileInfo.getLength());
        detail.setName(fileInfo.getName());
        SharedPojo sharedPojo = sharedMapper.findByPath(sharedPath);
        if (sharedPojo == null){
            log.error("获取分享文件详情失败 -- 路径[{}]未从数据库shared中查询到结果", sharedPath);
            throw new OnLineDiskException("请检查路径");
        }
        detail.setSharedTime(sharedPojo.getSharedTime().getTime());
        detail.setDownloadCount(sharedPojo.getNuDown());
        return detail;
    }

    /**
     * 若共享路径是目录，则使用此方法获取目录下的文件列表
     * @param sharedPath 查询路径，其某个节点的父路径应是共享目录，但本方法中不做相关判断
     * @return 文件列表
     */
    public List<OnlinediskFileDetail> getSharedFolderSub(String sharedPath){
        List<OnlinediskFileDetail> details = new ArrayList<>();
        List<SimpleFileInfo> fileInfoList = warehouse.ListFilesInfo(sharedPath);
        if (fileInfoList == null){
            log.error("查询共享目录文件列表失败 -- 具体失败原因请查看上几行日志");
        }
        for (SimpleFileInfo simpleFileInfo : fileInfoList) {
            OnlinediskFileDetail detail = OnlinediskFileDetail.build(simpleFileInfo);
            detail.setShear(true);
            details.add(detail);
        }
        return details;
    }

    /**
     * 读取目录
     */
    public List<SimpleFileInfo> listFolder(User user, String path) throws OnLineDiskException {
        // 判断用户是否存在
        OnLineDisk onLineDisk = onLineDiskMapper.findById(user.getId());
        if (null == onLineDisk){
            log.error("用户未开通云盘：{}", user);
            throw new OnLineDiskException("用户未开通云盘");
        }
        path = onLineDisk.getDirName()+ FilesUtil.separator + path;
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
     * 分享文件
     * @param filePath 文件路径
     * @param user 用户信息
     * @param sharedTime 共享时间
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public void shareFile(User user, String filePath, int sharedTime) throws OnLineDiskException {
        OnLineDisk onLineDiskData = findOnLineDiskData(user);
        String path = FilesUtil.join(new File(onLineDiskData.getDirName()).getName(), filePath);
        SimpleFileInfo fileInfo = warehouse.getFileInfo(path);
        if (fileInfo == null){
            log.error("文件分享失败 -- 文件不存在:{}", path);
            throw new OnLineDiskException("文件不存在");
        }
        // 若文件已经分享，则更新一下过期时间
        if (isShare(path)) {
            SharedPojo share = sharedMapper.findByPath(path);
            share.setSharedTime(DateUtil.addDate(sharedTime));
            sharedMapper.update(share);
            return;
        }
        // 若文件未被分享过，则执行插入操作
        SharedPojo sharedPojo = new SharedPojo();
        sharedPojo.setUserId(onLineDiskData.getUserId());
        // 写入路径为相对主仓库路径
        sharedPojo.setPath(path);
        sharedPojo.setIsFile(!fileInfo.getIsDirectory());
        sharedPojo.setSharedTime(DateUtil.addDate(sharedTime));
        sharedPojo.setNuDown(0);
        if (sharedMapper.insert(sharedPojo) != 1) {
            log.error("文件分享失败 -- 数据写入shared表失败");
            throw new OnLineDiskException("系统异常");
        }
    }

    /**
     * 获取分享文件，由于分享文件公开访问权限的，所以不需要获得访问用户信息。判断路径上文件是否为分享文件在调用本方法前做判断
     * @param sharedPath 文件路径，这个文件路径应是从分享表中读取的路径，指明了用户目录
     * @return 文件
     */
    @Transactional(rollbackFor = Exception.class)
    public Resource getSharedFile(String sharedPath) throws OnLineDiskException {
        // 判断分享文件是否过期
        SharedPojo shared = sharedMapper.findByPath(sharedPath);
        if (shared.getSharedTime().before(new Date())) {
            log.error("获取分享文件失败 -- 分享资源已过期，到期时间[{}]", DateUtil.simpleFormatDate(shared.getSharedTime()));
            throw new OnLineDiskException("文件已过期");
        }
        Resource resource = warehouse.getFile(sharedPath);
        if (resource == null){
            log.error("获取分享资源失败 -- 资源路径：{}", sharedPath);
            throw new OnLineDiskException("文件路径错误");
        }
        shared.setNuDown(shared.getNuDown()+1);
        sharedMapper.update(shared);
        return resource;
    }

    /**
     * 取消分享，需要先获取到要取消文件所在shared表的id
     * @param sharedId 分享id
     */
    @Transactional(rollbackFor = Exception.class)
    public void CancelSharing(int sharedId) throws OnLineDiskException {
        if (sharedMapper.delete(sharedId) != 1) {
            // 删除条数不为0，表中数据不存在
            log.error("取消文件分享失败 -- [{}]共享id不存在", sharedId);
            throw new OnLineDiskException("文件不存在");
        }
    }

    /**
     * 删除一个路径，此路径可能是文件或者目录
     */
    public boolean rmPath(User user, String path) throws OnLineDiskException {
        // 验证path
        if (StrUtil.isEmpty(path) || FilesUtil.separator.equals(path) || FilesUtil.winSeparator.equals(path)){
            log.error("云盘文件删除路径异常 -- 不允许删除根目录");
            return false;
        }
        OnLineDisk userDb = findOnLineDiskData(user);
        String dirName = userDb.getDirName();
        File file = new File(dirName);
        // 仓库对象使用的相对目录
        path = FilesUtil.join(file.getName(), path);
        // 获取删除路径占用大小
        SimpleFileInfo fileInfo = warehouse.getFileInfo(path);
        long size = fileInfo.getLength();
        //删除文件
        if (!warehouse.delete(path)) {
            log.error("云盘文件删除失败");
            return false;
        }
        //修改数据库
        userDb.setUseSize(userDb.getUseSize() - size);
        onLineDiskMapper.update(userDb);
        return true;
    }

    /**
     * 获取用户再onlinedisk表的数据，若标记为删除，则返回null
     * @param user 用户信息
     * @return 一条数据
     */
    private OnLineDisk findOnLineDiskData(User user) throws OnLineDiskException {
        OnLineDisk lineDisk = onLineDiskMapper.findById(user.getId());
        if (lineDisk == null || lineDisk.getShutoff()){
            log.error("用户未开通云盘 -- userid = {}", user.getId());
            throw new OnLineDiskException("用户未开通云盘");
        }
        return lineDisk;
    }

    /**
     * 判断是否分享
     */
    private boolean isShare(String path){
        SharedPojo share = sharedMapper.findByPath(path);
        return share != null;
    }


    /**
     * 写一些配置
     */
    private static class DefaultConf{
        /** 新建的云盘默认分配1G空间 */
        public static long allSize = 1073741824;
    }
}
