package com.recupera.item.back.recupera.config;

import static org.mockito.Mockito.mock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.recupera.item.back.recupera.Config.GoogleDriveConfig;
@TestConfiguration
public class GoogleDriveConfigTestConfig {
    @Bean
    public GoogleDriveConfig googleDriveConfig() {
        return mock(GoogleDriveConfig.class);
    }
}
