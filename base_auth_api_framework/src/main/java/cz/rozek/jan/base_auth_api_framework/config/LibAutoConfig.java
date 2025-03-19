package cz.rozek.jan.base_auth_api_framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import cz.rozek.jan.base_auth_api_framework.permissions.PermissionAutoloader;

@AutoConfiguration
@ComponentScan(basePackages = "cz.rozek.jan.base_auth_api_framework")
public class LibAutoConfig {
    
    @Autowired
    private PermissionAutoloader permissionAutoloader;
}