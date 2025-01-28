package com.noel.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cognito.client")
@Data

public class CognitoConfiguration {

    private String key;
    private String secret;
    private String region;
    private String poolId;

    public AWSCredentials getCredentials() {

        System.out.println(key);
        System.out.println(poolId);

        return new BasicAWSCredentials(key, secret);
    }

    public AWSCredentialsProvider getCredentialProvider() {
        return new AWSStaticCredentialsProvider(getCredentials());
    }

}

