package cz.rozek.jan.base_auth_api_framework.services.jwt;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

public interface IJwtService {
    JwtClaims getClaimsFromToken(String jwt, String keyId); 
    String generateJWT(JwtClaims claims, String keyId) throws JoseException;
    String generateJWT(String keyId) throws JoseException;
    JwtClaims getBasicJwtClaims();
}
