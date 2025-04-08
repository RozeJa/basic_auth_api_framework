package cz.rozek.jan.base_auth_api_framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import cz.rozek.jan.base_auth_api_framework.permissions.PermissionAutoloader;

@Component
public class PermissionAutoloaderRunner implements ApplicationRunner {
    
    @Autowired
    private PermissionAutoloader permissionAutoloader;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (permissionAutoloader != null) {
            boolean compleated = false;
            while (!compleated) {
                try {
                    permissionAutoloader.storePermissions();
                    compleated = true;
                } catch (Exception e) {
                    Thread.sleep(5000);
                }
            }
        }
    }
}
