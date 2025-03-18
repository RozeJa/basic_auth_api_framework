package cz.rozek.jan.base_auth_api_framework.security;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;

public class RSAKeyStorage implements IRSAKeyStorage {

    private int keyLength;    
	private File rootLocation;
	private String rootDir;

    private Map<String, PublicJsonWebKey> cache = new TreeMap<>();

    @Override
    public void setKeyLength(int keyLength) {
        this.keyLength = keyLength;
    }
    @Override
	public void setRootLocation(String rootLocation) {
		rootDir = rootLocation;
	}
    @Override
    public void init() {
		this.rootLocation = new File(rootDir);
        if (!rootLocation.isDirectory()) {
            if (!rootLocation.mkdirs()) {
                throw new RuntimeException("Could not initialize storage");
            }
        }
    }

    @Override
    public RsaJsonWebKey gainRsaJwk(String keyId) throws JoseException {
        // vytvor si objekt souboru
        File keyFile = new File(rootDir + keyId);

        // zkus najít klíč a načíst ho
        try (BufferedReader reader = new BufferedReader(new FileReader(keyFile))) {
            String keyJson = reader.readLine();

            RsaJsonWebKey key = (RsaJsonWebKey) PublicJsonWebKey.Factory.newPublicJwk(keyJson);
            cache.put(keyId, key);
                return key;
        } catch (IOException e) {
            RsaJsonWebKey key = generateJwk(keyId);
            cache.put(keyId, key);

            try {
                storeKey(key, keyFile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return key;
        }
    }

    /**
     * 
     * @param keyId keyId vráceného klíče bude mít tento parametr
     * @param keyFile soubor, do kterého se má ulozit klíč
     * @return vygenerovaný
     * @throws IOException problém se zápisem 
     * @throws JoseException problém s generobáním
     */
    private RsaJsonWebKey generateJwk(String keyId) throws JoseException {
        // Pokud klíč neexistuje, tak ho vygeneruj
        RsaJsonWebKey key = RsaJwkGenerator.generateJwk(keyLength);
        key.setKeyId(keyId);
    
        // vrat vygenerovaný klíč
        return key;
    }

    private void storeKey(RsaJsonWebKey key, File keyFile) throws IOException {
        // preved ho na json
        String jwkjson = key.toJson(JsonWebKey.OutputControlLevel.INCLUDE_PRIVATE);
        // a uloz ho
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(keyFile))) {
            writer.write(jwkjson);       
        }
    }
}
