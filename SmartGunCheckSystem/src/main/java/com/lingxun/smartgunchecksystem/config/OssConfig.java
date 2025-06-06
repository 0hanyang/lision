package com.lingxun.smartgunchecksystem.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
// import com.aliyun.oss.OSS;
// import com.aliyun.oss.OSSClientBuilder;
// import org.springframework.context.annotation.Bean;

@Data
@Configuration
@ConfigurationProperties(prefix = "file-upload.oss")
public class OssConfig {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    // private String region; // Optional

    // // Uncomment and add aliyun-sdk-oss dependency if using OSS
    // @Bean
    // public OSS ossClient() {
    //     if (endpoint == null || accessKeyId == null || accessKeySecret == null || bucketName == null) {
    //         // Log a warning or skip bean creation if not fully configured
    //         return null;
    //     }
    //     return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    // }
}
