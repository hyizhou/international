package top.hyizhou.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "top.hyizhou")
public class FormeworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(FormeworkApplication.class, args);
    }

}
