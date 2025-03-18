package cz.rozek.jan.base_auth_api_framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cz.rozek.jan.base_auth_api_framework.security.RSAKeyStorage;

@Configuration
public class RSAKeyStorageConfig {

    @Value("${spring.baaf.rsa-key-storage.key-length}")
    private int rsaKeylength;

    @Value("${spring.baaf.rsa-key-storage.dir}")
    private String dir;

    @Bean
    public RSAKeyStorage getRSAKeyStorage() {
        RSAKeyStorage rsaKeyStorage = new RSAKeyStorage();
        rsaKeyStorage.setKeyLength(rsaKeylength);
        rsaKeyStorage.setRootLocation(dir);
        rsaKeyStorage.init();

        return rsaKeyStorage;
    }
}
