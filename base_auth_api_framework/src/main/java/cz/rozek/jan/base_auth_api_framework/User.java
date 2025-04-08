package cz.rozek.jan.base_auth_api_framework;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import cz.rozek.jan.base_auth_api_framework.exceptions.ValidationException;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("users")
@Data
@NoArgsConstructor
public class User implements Entity {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String username;
    private String password;
    
    @Indexed(unique = true)
    private String email;
    private String activationCode;

    @DBRef
    private Set<Role> roles;
    
    private Set<String> loginJWTs;
    private Set<String> trustJWTs;

    private boolean activated = false;
    private boolean enabled = true;


    public void addRole(Role role) {
        if (roles == null) 
            roles = new TreeSet<>();
        roles.add(role);
    }
    public Role removeRole(Role role) {
        if (roles.remove(role)) {
            return role;
        }
        return null;
    }
    public Role getRole(String name) {
        try {
            return roles.stream().filter((r) -> r.getName().equals(name)).findFirst().get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    
    public boolean addLoginJWT(String loginJWT) {
        String token = BCrypt.hashpw(loginJWT, BCrypt.gensalt());

        if (loginJWTs == null) 
            loginJWTs = new TreeSet<>();

        return loginJWTs.add(token);
    } 
    public boolean removeLoginJWT(String loginJWT) {
        if (loginJWTs == null) 
            return false;

        String token = findToken(loginJWTs, loginJWT);

        return loginJWTs.remove(token);
    }
    public boolean containsLoginJWT(String loginJWT) {
        if (loginJWTs == null) 
            return false;

        return findToken(loginJWTs, loginJWT) == null;
    }

    private String findToken(Set<String> tokens, String JWT) {
        for (String token : tokens) {
            if (BCrypt.checkpw(JWT, token)) {
                return token;
            }
        }
        return null;
    }
    
    public boolean addTrustJWT(String trustJWT) {
        String token = BCrypt.hashpw(trustJWT, BCrypt.gensalt());

        if (trustJWTs == null) 
            trustJWTs = new TreeSet<>();

        return trustJWTs.add(token);
    } 
    public boolean removeTrustJWT(String trustJWT) {
        if (trustJWTs == null) 
            return false;

        String token = findToken(trustJWTs, trustJWT);

        return trustJWTs.remove(token);
    }
    public boolean containsTrustJWT(String trustJWT) {
        if (trustJWTs == null) 
            return false;

        return findToken(trustJWTs, trustJWT) == null;
    }

    @Override
    public void validate() throws ValidationException {
        if (username.isBlank()) 
            throw new ValidationException("Username can't be blank.");

        if (password.isBlank()) // validace hesla chybý
            throw new ValidationException("Password can't be blank.");

        if (email.isBlank()) // validace emailu chybý
            throw new ValidationException("Email can't be blank.");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User user = (User) obj;

            return this.id.equals(user.id);
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public void update(User data) {
        if (!data.password.isBlank())
            this.password = data.password;
        if (!data.email.isBlank()) 
            this.email = data.email;
            
        this.activated = data.activated;
        this.enabled = data.enabled;
    }

    public String findRolesNames() {
        StringBuilder sb  = new StringBuilder();

        Role[] rolesArr = (Role[]) roles.toArray();

        for (int i = 0; i < rolesArr.length; i++) {
            sb.append(rolesArr[i].getName());

            if (i + 1 < rolesArr.length) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("""
            User %s with email %s.
            His roles are %s.
            %s 
            %s

            """, username, email, findRolesNames(),
            !activated ? "is not activated" : "",
            !enabled ? "is not enabled": "");
    }
}
