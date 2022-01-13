package top.hyizhou.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "top.hyizhou")
@EnableTransactionManagement
public class FormeworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(FormeworkApplication.class, args);
    }

}
