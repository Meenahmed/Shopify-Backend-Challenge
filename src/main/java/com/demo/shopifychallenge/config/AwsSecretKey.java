package com.demo.shopifychallenge.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.aws")
@Getter
@Setter
@NoArgsConstructor
public class AwsSecretKey {
    private String secretKey;
    private String accessKey;
}
