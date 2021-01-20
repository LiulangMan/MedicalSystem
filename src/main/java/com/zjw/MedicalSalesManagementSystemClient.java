package com.zjw;

import com.zjw.config.FontConfiguration;
import com.zjw.controller.LoginController;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan(basePackages = "com.zjw.mapper")
public class MedicalSalesManagementSystemClient {


    public static void main(String[] args) {
        //获取Context
        SpringApplicationBuilder builder = new SpringApplicationBuilder(MedicalSalesManagementSystemClient.class);
        ConfigurableApplicationContext context = builder.headless(false).web(WebApplicationType.NONE).run(args);
        //Login入口
        LoginController loginController = context.getBean(LoginController.class);
        loginController.start();

    }
}
