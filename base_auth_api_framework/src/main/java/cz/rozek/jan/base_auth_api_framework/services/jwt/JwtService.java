package cz.rozek.jan.base_auth_api_framework.services.jwt;

import java.util.HashMap;
import java.util.Map;

import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

import cz.rozek.jan.base_auth_api_framework.security.RSAKeyStorage;
import cz.rozek.jan.base_auth_api_framework.exceptions.SecurityException;

public class JwtService implements IJwtService {

    private RSAKeyStorage keys;
    private Map<String, JwtConsumer> jwtConsumers = new HashMap<>();
    
    private String issuer;
    private String expectedIssuer;
    
    private String[] audience;
    private String[] expectedAudience;
    
    @Override
    public JwtClaims getClaimsFromToken(String jwt, String jwtCustomerId) {
        JwtConsumer jwtConsumer = jwtConsumers.get(jwtCustomerId);

        try {
            return jwtConsumer.processToClaims(jwt);
        } catch (InvalidJwtException e) {
            throw new SecurityException("Invalid Token.");
        } catch (NullPointerException e) {
            throw new SecurityException("Customer not found.");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public String generateJWT(JwtClaims claims, String keyId) throws JoseException {

        RsaJsonWebKey jwtKey = keys.gainRsaJwk(keyId);

        if (jwtKey == null)
            throw new SecurityException("RsaKey not found.");

        claims.setAudience(audience);
        claims.setIssuer(issuer);

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());

        jws.setKey(jwtKey.getPrivateKey());
        jws.setKeyIdHeaderValue(jwtKey.getKeyId());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_PSS_USING_SHA256);

        return jws.getCompactSerialization();
    }

    @Override
    public String generateJWT(String keyId) throws JoseException {
        JwtClaims claims = getBasicJwtClaims();
        claims.setAudience(audience);
        claims.setIssuer(issuer);
        return generateJWT(claims, keyId);
    }

    @Override
    public JwtClaims getBasicJwtClaims() {
        JwtClaims claims = new JwtClaims();
        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();

        return claims;
    }

    public void addCustomer(JwtConsumerBuilder jwtConsumerBuilder, String keyId) throws JoseException {
        RsaJsonWebKey jwtKey = keys.gainRsaJwk(keyId);
    
        JwtConsumer jwtConsumer = jwtConsumerBuilder
            .setExpectedAudience(expectedAudience)
            .setExpectedIssuer(expectedIssuer)
            .setVerificationKey(jwtKey.getKey())
            .setJwsAlgorithmConstraints(ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_PSS_USING_SHA256)
            .build();
    
        jwtConsumers.put(keyId, jwtConsumer);
    }

    public static JwtConsumerBuilder getBasicJwtConsumerBuilder() {
        return new JwtConsumerBuilder()
                .setRequireSubject();
    }



    public void setKeys(RSAKeyStorage keys) {
        this.keys = keys;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public void setExpectedIssuer(String expectedIssuer) {
        this.expectedIssuer = expectedIssuer;
    }

    public void setAudience(String[] audience) {
        this.audience = audience;
    }

    public void setExpectedAudience(String[] expectedAudience) {
        this.expectedAudience = expectedAudience;
    }
}
