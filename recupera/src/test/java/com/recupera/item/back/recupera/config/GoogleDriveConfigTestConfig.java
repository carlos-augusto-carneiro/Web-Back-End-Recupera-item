package com.recupera.item.back.recupera.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import static org.mockito.Mockito.mock;

@TestConfiguration
public class GoogleDriveConfigTestConfig {
    @Bean
    public GoogleDriveConfig googleDriveConfig() {
        return mock(GoogleDriveConfig.class);
    }
}
