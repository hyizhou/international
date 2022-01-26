package top.hyizhou.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "top.hyizhou")
@EnableScheduling
//@EnableTransactionManagement
public class FormeworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(FormeworkApplication.class, args);
    }

}
