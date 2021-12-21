package top.hyizhou.framework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import top.hyizhou.framework.entity.Resp;
import top.hyizhou.framework.entity.SimpleFileInfo;
import top.hyizhou.framework.utils.StrUtil;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
    /**
     * 对外开放的目录，key是外部访问的id
     */
    @Value("#{${file.download.map}}")
    private Map<String, String> dirMap;
    /** 上传文件存储目录 */
    @Value("${file.upload.path}")
    private String uploadPath;
    /** 上传目录文件id对应关系，TODO 应在初始化时加载目录中文件 */
    private final Map<String, File> uploadMap;

    public FileService(){
        uploadMap = new HashMap<>();
    }

    /**
     * 像输出流中写入数据
     *
     * @param out 输出流
     * @return 文件名
     */
    public String writeStream(OutputStream out, String dirId, String path) throws IOException {
        if (!StringUtils.hasLength(path)) {
            throw new IOException("下载路径为空");
        }
        File file = absoluteFile(dirId, path);
        if (file.isDirectory()) {
            throw new IOException("下载路径为目录：" + file.getAbsolutePath());
        }
        log.info("将读取下载文件：{}", file.getAbsolutePath());
        try (InputStream fileStream = new FileInputStream(file)) {
            int len;
            byte[] b = new byte[1024];
            while ((len = fileStream.read(b)) > 0) {
                out.write(b, 0, len);
            }
        } catch (FileNotFoundException e) {
            log.error("文件读取失败，错误详情：", e);
            throw new FileNotFoundException("文件读取失败，可能是没有权限，或者查看上面日志");
        }
        log.info("下载文件写入输出流完毕");
        return file.getName();
    }


    /**
     * 前端虚拟路径传过来，解析成物理地址上的路径
     *
     * @return 真实地址
     */
    private File absoluteFile(String dirId, String path) throws IOException {
        String dirPath = dirMap.get(dirId);
        if (dirPath == null) {
            log.error("目录id不存在 - dirId:{}; path:{}", dirId, path);
            throw new IOException("目录id不存在：" + dirId);
        }
        File file = new File(dirPath + File.separator + path);
        if (!file.exists()) {
            log.error("指定路径不存在 - dirId:{}; path:{}; filePath:{}", dirId, path, file.getPath());
            throw new IOException("指定路径不存在：" + file.getPath());
        }
        return file;
    }

    /**
     * 判断请求路径是否为文件
     *
     * @param dirId 开放目录id
     * @param path  请求路径相对地址
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
        throw new IOException("路径所指既不是普通文件也不是目录：" + file.getPath());
    }

    /**
     * 返回请求目录下的文件列表。先判断是否为目录，再调用此方法，因为本方法没有进行是否是目录判断。
     *
     * @param dirId 开放目录id
     * @param path  请求路径相对地址
     * @return 文件列表
     */
    public List<SimpleFileInfo> getDirectoryList(String dirId, String path) throws IOException {
        File file = absoluteFile(dirId, path);
        log.info("展开目录路径：{}", file.getAbsolutePath());
        List<SimpleFileInfo> req = new ArrayList<>();
        File[] subFiles = file.listFiles();
        if (subFiles == null) {
            return null;
        }
        for (File subFile : subFiles) {
            req.add(new SimpleFileInfo(subFile.getName(), subFile.isDirectory()));
        }
        return req;
    }


    /**
     * 处理文件上传的具体方法
     * 理论处理流程：得到文件，在文件名上面添加必要信息，重命名文件后存储，将文件详情返回
     * id值生成规则：时间戳+3位随机数
     * 文件名生成规则：id+'-'+下载次数+'-'+文件名，即使用短横线分隔
     *
     * 问题：将id、下载次数这些放到数据库更好，但是目前（21/12/21）还没有加数据库，所有暂时这样吧；
     *      文件名存储数据方式需要IO操作，太慢了
     * @param file 文件流
     * @return 响应对象
     */
    public Resp<String[]> upload(MultipartFile file) {
        if (null == file || file.isEmpty()) {
            return Resp.failed("400", "file not exists");
        }
        // 调用生成文件名的方法
        String saveName = createFileName(file.getOriginalFilename());
        String[] analyze = analyzeFileName(saveName);
        if (analyze == null){
            log.error("上传文件文件名生成异常");
            throw new RuntimeException("文件名生成异常");
        }
        String saveDir = uploadPath;
        if (saveDir == null) {
            log.error("未配置saveDir路径，即文件上传保存路径");
            return Resp.failed("500", "error");
        }
        File currFile = new File(saveDir + File.separator + saveName);
        if (!currFile.getParentFile().exists()) {
            if (!currFile.getParentFile().mkdirs()) {
                log.error("创建文件存储目录失败：{}", currFile.getParentFile().getAbsolutePath());
                return Resp.failed("500", "Error");
            }
        }
        try {
            file.transferTo(currFile);
        } catch (IOException e) {
            log.error("存储文件异常:", e);
            return Resp.failed("500", "存储文件异常");
        }
        String id = analyze[0];
        // 将文件id存储到map，加快索引
        uploadMap.put(id, currFile);
        log.info("存储文件[{}]成功：{}", file.getOriginalFilename(), currFile.getAbsolutePath());
        SimpleDateFormat time = new SimpleDateFormat("yyyy年MM月dd HH:mm:ss");
        analyze[1] = time.format(Long.valueOf(analyze[1]));
        return Resp.success(analyze);
    }

    /**
     * 通过id下载上传文件
     * @param id 上传文件时生成的id
     * @return 返回文件资源
     */
    public Resource download(String id) {
        File file = uploadMap.get(id);
        return new PathResource(file.toPath());

    }


    /**
     * 根据上传文件名生成服务器存储文件名
     * @param file 上传文件原始名称
     * @return 处理后文件名规范:[id]-[下载次数]-[真实文件名]
     */
    private String createFileName(String file){
        String join = "-";
        String id = System.currentTimeMillis() + String.format("%3d", (int) (Math.random() * 1000)).replace(" ", "0");
        return id+join+"0"+join+file;
    }

    private String increaseDownloadNum(String file){
        String[] analyse = analyzeFileName(file);
        if (analyse == null){
            throw new RuntimeException("文件名不正确: "+file);
        }
        int newNum = Integer.parseInt(analyse[2]) + 1;
        return analyse[0]+"-"+newNum+"-"+analyse[3];
    }

    public String readName(String fileName){
        String[] readName = analyzeFileName(fileName);
        if (readName == null){
            log.error("文件名解析失败，请查看上面错误日志");
            throw new RuntimeException("文件名解析失败");
        }
        return readName[3];
    }

    /**
     * 解析上传文件存储的文件名，从中提取出有用信息
     * @param fileName 文件名，规范文件名:[id]-[下载次数]-[真实文件名]
     * @return 数组，里面数据为:[id，存储时间，下载次数，文件原始名]，若为null则表示文件名不规范
     */
    private String[] analyzeFileName(String fileName){
        // 分隔符个数
        int splitNum = 3;
        // id长度
        int idLen = 16;
        if (StrUtil.isEmpty(fileName)) {
            log.error("所解析扥文件名为空");
            return null;
        }
        String[] split = fileName.split("-");
        // 分割后少于三份，则文件名有问题，大于三份是允许的，文件实际名称中可能也存在"-"
        if (split.length < splitNum){
            log.error("文件名命名不规范：{}", fileName);
            return null;
        }
        // 判断id规范，判断的目的防止其他文件不小心进入上传目录
        if (split[0].length() != idLen || !StrUtil.isInteger(split[0])){
            log.error("文件id不规范(长度应16且全为数字)，文件名：[{}]、", fileName);
            return null;
        }
        // 下载次数不为数字或小于0
        if (!StrUtil.isInteger(split[1]) || Integer.parseInt(split[1]) < 0){
            log.error("文件下载次数异常 - 文件名：{}",fileName);
            return null;
        }
        String id = split[0];
        String downNum = split[1];
        String fileRealName = fileName.substring(StrUtil.indexOf(fileName, "-", 2)+1);
        // 取十三位时间戳
        String saveTime = id.substring(0, 13);
        return new String[]{id, saveTime, downNum, fileRealName};
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(String.format("%3d", (int) (Math.random() * 1000)).replace(" ", "0"));
        }

    }

}
