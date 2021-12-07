package top.hyizhou.framework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import top.hyizhou.framework.entity.Resp;
import top.hyizhou.framework.entity.SimpleFileInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文件处理业务Service
 *
 * @author huanggc
 * @date 2021/11/8 16:42
 */
@Service
public class FileService {
    private final Logger log = LoggerFactory.getLogger(FileService.class);
    /** 对外开放的目录，key是外部访问的id */
    @Value("#{${download.dirMap}}")
    private Map<String, String> dirMap;

    /**
     * 像输出流中写入数据
     *
     * @param out 输出流
     * @return 文件名
     */
    public String pushFileStream(OutputStream out, String dirId, String path) throws IOException {
        if (!StringUtils.hasLength(path)){
            throw new IOException("下载路径为空");
        }
        File file = absoluteFile(dirId, path);
        if (file.isDirectory()){
            throw new IOException("下载路径为目录："+file.getAbsolutePath());
        }
        log.info("将读取下载文件：{}", file.getAbsolutePath());
        try(InputStream fileStream = new FileInputStream(file)) {
            int len;
            byte[] b = new byte[1024];
            while ((len = fileStream.read(b))>0){
                out.write(b,0,len);
            }
        }catch (FileNotFoundException e){
            log.error("文件读取失败，错误详情：",e);
            throw new FileNotFoundException("文件读取失败，可能是没有权限，或者查看上面日志");
        }
        log.info("下载文件写入输出流完毕");
        return file.getName();
    }



    /**
     * 前端虚拟路径传过来，解析成物理地址上的路径
     * @return 真实地址
     */
    private File absoluteFile(String dirId, String path) throws IOException {
        String dirPath = dirMap.get(dirId);
        if (dirPath == null){
            log.error("目录id不存在 - dirId:{}; path:{}", dirId, path);
            throw new IOException("目录id不存在："+dirId);
        }
        File file = new File(dirPath + File.separator + path);
        if (!file.exists()){
            log.error("指定路径不存在 - dirId:{}; path:{}; filePath:{}", dirId, path, file.getPath());
            throw new IOException("指定路径不存在："+file.getPath());
        }
        return file;
    }

    /**
     * 判断请求路径是否为文件
     * @param dirId 开放目录id
     * @param path 请求路径相对地址
     * @return true则是文件，false则大概率是目录
     * @throws IOException 当目录id或相对路径不存在或路径所指文件不是普通文件
     */
    public boolean isFile(String dirId, String path) throws IOException {
        File file = absoluteFile(dirId, path);
        if (file.isDirectory()) {
            return false;
        }
        if (file.isFile()) {
            return true;
        }
        log.error("路径所指文件不是普通文件（可能外接设备）：{}", file.getAbsolutePath());
        throw new IOException("路径所指既不是普通文件也不是目录："+file.getPath());
    }

    /**
     * 返回请求目录下的文件列表。先判断是否为目录，再调用此方法，因为本方法没有进行是否是目录判断。
     * @param dirId 开放目录id
     * @param path 请求路径相对地址
     * @return 文件列表
     */
    public Resp<List<SimpleFileInfo>> getListFile(String dirId, String path) throws IOException {
        File file = absoluteFile(dirId, path);
        log.info("展开完整路径：{}", file.getAbsolutePath());
        List<SimpleFileInfo> req = new ArrayList<>();
        File[] subFiles = file.listFiles();
        if (subFiles == null){
            return null;
        }
        for (File subFile : subFiles) {
            req.add(new SimpleFileInfo(subFile.getName(), subFile.isDirectory()));
        }
        return Resp.success(req);
    }


    /**
     * 处理文件上传的具体方法
     * 理论处理流程：得到文件，存储文件，生成id，返回id。用户通过id取得文件
     * @param file 文件流
     * @return 响应对象
     */
    public Resp<String> upload(MultipartFile file){
        if (null == file || file.isEmpty()){
            return Resp.failed("400", "file not exists");
        }
        // 通过毫秒数生成唯一文件名
        String saveName = System.currentTimeMillis() + "." + file.getOriginalFilename();
        String saveDir = dirMap.get("saveDir");
        if (saveDir == null){
            log.error("未配置saveDir路径，即文件上传保存路径");
            return Resp.failed("500", "error");
        }
        File currFile = new File(saveDir + File.separator + saveName);
        if (!currFile.getParentFile().exists()) {
            currFile.getParentFile().mkdirs();
        }
        try {
            file.transferTo(currFile);
        } catch (IOException e) {
            log.error("存储文件异常:",e);
            return Resp.failed("500", "存储文件异常");
        }
        return Resp.success(saveName);
    }



}
