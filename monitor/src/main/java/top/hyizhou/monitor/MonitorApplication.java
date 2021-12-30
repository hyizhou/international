package top.hyizhou.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MonitorApplication {

    public static void main(String[] args) {
        System.out.println("启动器被调用了");
        SpringApplication.run(MonitorApplication.class, args);
    }

}
