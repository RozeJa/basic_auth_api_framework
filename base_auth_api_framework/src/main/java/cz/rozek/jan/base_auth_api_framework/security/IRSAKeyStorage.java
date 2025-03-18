package cz.rozek.jan.base_auth_api_framework.security;

import java.io.IOException;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.lang.JoseException;

public interface IRSAKeyStorage {
    void setKeyLength(int keyLength);
	void setRootLocation(String rootLocation);
    void init();

    /**
     * Metoda se pokusí načíst klíč podle keyId, pokud se jí ho nepodaří načíst, vygeneruje si ho, uloží a vrátí. Klíč bude uložen pod keyId. Vrácený klíč bude mít keyId nastavené na to co dostala metoda v parametru
     * @param keyId keyId vráceného klíče bude mít tento parametr
     * @return vygenerovaný / načtený klíč
     * @throws IOException problém se čtením / zápisem 
     * @throws JoseException problém s generobáním 
     */
    RsaJsonWebKey gainRsaJwk(String keyId) throws JoseException;
}
