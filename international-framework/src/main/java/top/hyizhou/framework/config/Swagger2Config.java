package top.hyizhou.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * swagger2配置
 * @author hyizhou
 * @date 2022/1/14 15:29
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket buildDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("API")
                .apiInfo(new ApiInfo(
                        // 标题
                        "international",
                        // 描述
                        "描述",
                        // 版本
                        "1.01",
                        // 服务条款
                        "http://www.baidu.top",
                        // 作者信息
                        new Contact(
                                // 作者名
                                "hyizhou",
                                // 作者网站
                                "http://www.baidu.top/",
                                // 邮件
                                "null"),
                        // 许可证
                        "Apache 2.0",
                        // 许可证版本
                        "http://www.apache.org/license/license-2.0",
                        // 供应商列表
                        new ArrayList()
                ))
                .pathMapping("/")
                .select()
                    // 配置要扫描哪些接口
                    // basePackage：按包扫描，any：都扫描、none：不扫描，
                    // withClassAnnotation：按照给定的类注解扫描，withMethodAnnotation：根据给定的方法注解扫描
                    .apis(RequestHandlerSelectors.basePackage("top.hyizhou.framework.control"))
                    // 路径过滤
                    // any：所有路径都放行；none：都不放行；regex：按正则匹配；ant：可使用**进行路径匹配
                    .paths(PathSelectors.any())
                    .build()
                .enable(true);
    }

}
