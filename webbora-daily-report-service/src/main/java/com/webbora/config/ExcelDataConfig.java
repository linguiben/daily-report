package com.webbora.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * @desc: TODO
 * @author: Jupiter.Lin
 * @date: 2025/4/27
 */
@Getter@Setter
@Component
@ConfigurationProperties(prefix = "excel.data")
public class ExcelDataConfig {
    private Map<String, Map<String, String>> exhibitionMap;
}