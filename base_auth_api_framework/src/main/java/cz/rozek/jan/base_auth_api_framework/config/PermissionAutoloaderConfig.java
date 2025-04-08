package cz.rozek.jan.base_auth_api_framework.config;

import org.jose4j.jwt.JwtClaims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import cz.rozek.jan.base_auth_api_framework.HeaderItems;
import cz.rozek.jan.base_auth_api_framework.permissions.PermissionAutoloader;
import cz.rozek.jan.base_auth_api_framework.services.auth.IAuthentizationService;
import cz.rozek.jan.base_auth_api_framework.services.jwt.IJwtService;

@Configuration
public class PermissionAutoloaderConfig {

    @Value("${spring.baaf.permissions.root-package}")
    private String rootPackage;

    @Value("${spring.baaf.permissions.permission-server-uri:none}")
    private String uri;
    
    @Bean
    public PermissionAutoloader getPermissionAutoloader(ApplicationContext context, IJwtService jwtService) {

        if (uri.equals("none")) {
            return null;
        } 

        PermissionAutoloader autoloader = new PermissionAutoloader();
        autoloader.setContext(context);
        autoloader.setRootPackage(rootPackage);

        autoloader.setHandler((permissions) -> {
            RestClient restClient = RestClient.create(uri);

            JwtClaims claims = jwtService.getBasicJwtClaims();
            claims.setSubject(rootPackage);

            try {
                ResponseEntity<Void> responce = restClient
                    .post()
                    .header(HeaderItems.AUTHORIZATION, jwtService.generateJWT(claims, IAuthentizationService.PERMISSION_TOKEN))
                    .accept(MediaType.APPLICATION_JSON)
                    .body(permissions)
                    .retrieve()
                    .toBodilessEntity();

                if (!responce.getStatusCode().equals(HttpStatus.OK)) {
                    throw new Exception(String.format("Storing permissing on URI: %s failed with status: %s", uri, responce.getStatusCode().value()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return autoloader;
    }
}