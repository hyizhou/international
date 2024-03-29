package top.hyizhou.framework.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.hyizhou.framework.entity.User;
import top.hyizhou.framework.mapper.UsersMapper;
import top.hyizhou.framework.utils.container.LoggedOnContainer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * @author hyizhou
 * @date 2021/11/26 11:02
 */
@Controller
public class TestControl {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private LoggedOnContainer container;
    private final Logger logger = LoggerFactory.getLogger(TestControl.class);
    @RequestMapping("/test/1")
    @ResponseBody
    public String test1(Authentication authentication){
        System.out.println(authentication.getName());
        System.out.println(authentication.getAuthorities().toString());
        return "ok";
    }

    @RequestMapping("/test/2")
    @ResponseBody
    public String test2(@CurrentSecurityContext SecurityContext context){
        System.out.println(context.getAuthentication().getName());
        System.out.println(context.getAuthentication().getAuthorities().toString());
        return "ok";
    }

    @RequestMapping("/test/3")
    @ResponseBody
    public String test2(@AuthenticationPrincipal UserDetails user){
        System.out.println(user.getUsername());
        System.out.println(user.getAuthorities().toString());
        return "ok";
    }

    @RequestMapping("/test/4")
    @ResponseBody
    public String test2(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getName());
        System.out.println(authentication.getAuthorities().toString());
        return "ok";
    }

    @RequestMapping("/test/mybatis")
    @ResponseBody
    public String testMyBaits(){
        User user = new User();
        user.setName("小明");
        user.setPassword("1234");
        user.setPhone("12306");
        user.setAccountName("test"+Math.random());
        usersMapper.insertOne(user);
        user.setName("小原");
        System.out.println("更新条数："+usersMapper.update(user));
        return "ok";
    }

    /**
     * 查看登录信息容器所有数据
     */
    @RequestMapping("/test/getLoggedOn")
    @ResponseBody
    public Map<String, LoggedOnContainer.LoggedOnInfo> getLoggedOn(){
        return container.getAll();
    }

    // 断点下载
    @RequestMapping("/test/download/{name}")
    public void getDownload(@PathVariable String name, HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
        // Get your file stream from wherever.
        logger.info("name="+name);
        String fullPath = ResourceUtils.getURL("classpath:").getPath() + "static/ludashisetup.exe";
        logger.info("下载路径:"+fullPath);
        File downloadFile = new File(fullPath);

        ServletContext context = request.getServletContext();
        // get MIME type of the file
        String mimeType = context.getMimeType(fullPath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }

        // set content attributes for the response
        response.setContentType(mimeType);
        // response.setContentLength((int) downloadFile.length());

        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        response.setHeader(headerKey, headerValue);
        // 解析断点续传相关信息
        response.setHeader("Accept-Ranges", "bytes");
        long downloadSize = downloadFile.length();
        long fromPos = 0, toPos = 0;
        if (request.getHeader("Range") == null) {
            response.setHeader("Content-Length", downloadSize + "");
        } else {
            // 若客户端传来Range，说明之前下载了一部分，设置206状态(SC_PARTIAL_CONTENT)
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            String range = request.getHeader("Range");
            String bytes = range.replaceAll("bytes=", "");
            String[] ary = bytes.split("-");
            fromPos = Long.parseLong(ary[0]);
            if (ary.length == 2) {
                toPos = Long.parseLong(ary[1]);
            }
            int size;
            if (toPos > fromPos) {
                size = (int) (toPos - fromPos);
            } else {
                size = (int) (downloadSize - fromPos);
            }
            response.setHeader("Content-Length", size + "");
            downloadSize = size;
        }
        // Copy the stream to the response's output stream.
        RandomAccessFile in = null;
        OutputStream out = null;
        try {
            in = new RandomAccessFile(downloadFile, "rw");
            // 设置下载起始位置
            if (fromPos > 0) {
                in.seek(fromPos);
            }
            // 缓冲区大小
            int bufLen = (int) (downloadSize < 2048 ? downloadSize : 2048);
            byte[] buffer = new byte[bufLen];
            int num;
            int count = 0; // 当前写到客户端的大小
            out = response.getOutputStream();
            while ((num = in.read(buffer)) != -1) {
                out.write(buffer, 0, num);
                count += num;
                //处理最后一段，计算不满缓冲区的大小
                if (downloadSize - count < bufLen) {
                    bufLen = (int) (downloadSize-count);
                    if(bufLen==0){
                        break;
                    }
                    buffer = new byte[bufLen];
                }
            }
            response.flushBuffer();
        } catch (IOException e) {
            logger.info("数据被暂停或中断。");
//            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.info("数据被暂停或中断。");
//                    e.printStackTrace();
                }
            }
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.info("数据被暂停或中断。");
//                    e.printStackTrace();
                }
            }
        }
    }
}
