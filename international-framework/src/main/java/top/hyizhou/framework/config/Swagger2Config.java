package top.hyizhou.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("top.hyizhou.framework.control"))
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder()
                        .title("swgger2测试")
                        .contact(new Contact("啊啊啊啊","blog.csdn.net","aaa@gmail.com"))
                        .build()
                );
    }

    private ApiInfo buildApiInfo(){
        return new ApiInfoBuilder()
                //页面标题，导入postman时必须有该项配置
                .title("模拟创建仿真数据")
                //版本号，导入postman时必须有该项配置
                .version("1.0")
                .description("视频大数据平台基线V1.1-区域分析-业务数据构造")
                //创建人
                .contact(new Contact("jiangmy","http://localhost:80/swagger-ui.html","jiangmy@dragoninfo.com.cn"))
                .build();
    }
}
