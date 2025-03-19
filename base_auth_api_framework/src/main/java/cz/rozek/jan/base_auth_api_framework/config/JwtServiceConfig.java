package cz.rozek.jan.base_auth_api_framework.config;

import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cz.rozek.jan.base_auth_api_framework.security.RSAKeyStorage;
import cz.rozek.jan.base_auth_api_framework.services.auth.IAuthentizationService;
import cz.rozek.jan.base_auth_api_framework.services.jwt.JwtService;

@Configuration
public class JwtServiceConfig {
    
    @Value("${spring.baaf.jwt.issuer}")
    private String issuer;
    @Value("${spring.baaf.jwt.expected-issuer}")
    private String expectedIssuer;
    
    @Value("${spring.baaf.jwt.audience}")
    private String[] audience;
    @Value("${spring.baaf.jwt.expected-audience}")
    private String[] expectedAudience;
    
    @Bean
    public JwtService getJwtService(RSAKeyStorage keys) throws JoseException {

        JwtService jwtService = new JwtService();
        jwtService.setKeys(keys);
        jwtService.setAudience(audience);
        jwtService.setExpectedAudience(expectedAudience);
        jwtService.setIssuer(issuer);
        jwtService.setExpectedIssuer(expectedIssuer);

        jwtService.addCustomer(JwtService.getBasicJwtConsumerBuilder(), IAuthentizationService.ACCESS_TOKEN);
        jwtService.addCustomer(JwtService.getBasicJwtConsumerBuilder(), IAuthentizationService.LOGIN_TOKEN);
        jwtService.addCustomer(JwtService.getBasicJwtConsumerBuilder(), IAuthentizationService.THRUST_TOKEN);
        jwtService.addCustomer(JwtService.getBasicJwtConsumerBuilder(), IAuthentizationService.PERMISSION_TOKEN);

        return jwtService;
    }
}
