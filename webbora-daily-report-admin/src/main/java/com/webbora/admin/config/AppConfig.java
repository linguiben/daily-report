package com.webbora.admin.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * @desc: TODO
 * @author: Jupiter.Lin
 * @date: 2025/4/26
 */
@Slf4j
@Configuration
public class AppConfig {

    @Bean
    public String applicationHomePath() {
        ApplicationHome applicationHome = new ApplicationHome(getClass());
        File jarFile = applicationHome.getSource();
        log.info("applicationHome:{}", applicationHome);
        return jarFile.getParentFile().toString();
    }
}
