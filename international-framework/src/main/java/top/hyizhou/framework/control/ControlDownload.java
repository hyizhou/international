package top.hyizhou.framework.control;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 下载测试
 * @author huanggc
 * @date 2021/11/8 15:26
 */
@RestController
@RequestMapping("/download")
@CrossOrigin(origins = "*")
public class ControlDownload {

    @GetMapping("/fineDownload/{name}")
    public void fileDownload(@PathVariable("name")String name ,HttpServletResponse response){
        System.out.println("name="+name);
        System.out.println("收到下载请求");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=aa.txt");
        try {
            FileInputStream file = new FileInputStream("D://aa.txt");
            OutputStream out = response.getOutputStream();
            byte[] b = new byte[1024];
            int length;
            while ((length = file.read(b))>0){
                out.write(b,0,length);
            }
            out.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
