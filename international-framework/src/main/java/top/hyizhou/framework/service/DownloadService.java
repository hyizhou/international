package top.hyizhou.framework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Map;

/**
 * 文件下载业务Service
 *
 * @author huanggc
 * @date 2021/11/8 16:42
 */
@Service
public class DownloadService {
    private final Logger log = LoggerFactory.getLogger(DownloadService.class);
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
            throw new IOException("目录id["+dirId+"]不存在");
        }
        File file = new File(dirPath + File.separator + path);
        if (!file.exists()) {
            throw new IOException("指定路径不存在，路径："+file.getPath());
        }
        return file;
    }

}
