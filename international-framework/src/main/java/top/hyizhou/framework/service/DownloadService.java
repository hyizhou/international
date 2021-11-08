package top.hyizhou.framework.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件下载业务Service
 * @author huanggc
 * @date 2021/11/8 16:42
 */
@Service
public class DownloadService {
    /** 存储文件与id的关系，存储结构为：id->文件路径 */
    Map<String, String> dict;
    /** 需要展示的下载文件所在目录列表 */
    @Value("#{'${download.dirs}'.split(',')}")
    private List<String> dirs;

    public DownloadService(){
        dict = new HashMap<>();

    }
}
