package cn.edu.jmu.dvs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@MapperScan("cn.edu.jmu.dvs.mapper")
@SpringBootApplication
public class DvsApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(DvsApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(DvsApplication.class, args);
    }

}
