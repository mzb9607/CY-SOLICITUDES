package com.bancolombia.crediya.sqs.sender.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapters.sqs")
public record SQSSenderProperties(
     String region,
     String queueUrl,
     String endpoint,
     String accessKey,
     String secretKey,
     String profileName){
}
