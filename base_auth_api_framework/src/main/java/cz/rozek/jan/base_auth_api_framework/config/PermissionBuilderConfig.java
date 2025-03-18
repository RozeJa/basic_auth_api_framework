package cz.rozek.jan.base_auth_api_framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cz.rozek.jan.base_auth_api_framework.permissions.PermissionBuilder;

@Configuration
public class PermissionBuilderConfig {
    
    @Bean
    public PermissionBuilder getPermissionBuilder() {
        PermissionBuilder pb = new PermissionBuilder();

        return pb;
    }
}
