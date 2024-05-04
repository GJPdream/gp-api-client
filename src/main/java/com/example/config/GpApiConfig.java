package com.example.config;

import com.example.client.SdkClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix = "gp.api")
@ComponentScan("com.example.client")
public class GpApiConfig {
    private String accessKey;
    private String secretKey;

    @Bean
    public SdkClient sdkClient() {
        return new SdkClient(accessKey, secretKey);
    }
}
