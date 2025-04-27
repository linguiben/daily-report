package com.webbora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

/**
* @Description: 启动类
* @author: Jupiter.Lin
* @version: V1.0 
* @date: 2021年6月3日 下午3:45:12
*/
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        // 1. 返回IOC容器
        ApplicationContext ctx = SpringApplication.run(App.class, args);

        // 2. 查看容器内的beans
//        String[] names = ctx.getBeanDefinitionNames();
//        Arrays.asList(names).stream().forEach(System.out::println);

    }
}
