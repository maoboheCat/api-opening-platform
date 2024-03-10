package com.cola.interfaceclientsdk;

import com.cola.interfaceclientsdk.client.ApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Maobohe
 * @createData 2024/3/3 20:02
 */
@Configuration
@ConfigurationProperties("interface.api.client")
@Data
@ComponentScan
public class InterfaceApiClientConfig {

    private String accessKey;

    private String secretKey;

    @Bean
    public ApiClient interfaceApiClient() {
        return new ApiClient(accessKey, secretKey);
    }


}
