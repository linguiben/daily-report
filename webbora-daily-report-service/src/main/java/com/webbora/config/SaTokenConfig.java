package com.webbora.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @desc: TODO
 * @author: Jupiter.Lin
 * @date: 2025/4/27
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    @Bean
    public SaInterceptor getSaInterceptor() {
        return new SaInterceptor(handler -> {
            // 登录验证
            StpUtil.checkLogin();
        });
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加 Sa-Token 拦截器
        registry.addInterceptor(getSaInterceptor())
                .addPathPatterns("/**") // 拦截所有路径
                .excludePathPatterns("/login", "/","/index"); // 排除登录接口和首页
    }
}
