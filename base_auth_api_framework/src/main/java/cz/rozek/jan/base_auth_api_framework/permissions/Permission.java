package cz.rozek.jan.base_auth_api_framework.permissions;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import cz.rozek.jan.base_auth_api_framework.Entity;
import cz.rozek.jan.base_auth_api_framework.exceptions.ValidationException;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("permissions")
@Data
@NoArgsConstructor
public class Permission implements Comparable<Permission>, Entity {
    
    @Id
    private String id;
    private String name;
    private String action;
    
    private boolean enabled = true;

    public Permission setName(String name) {
        this.name = name;
        return this;
    }

    public Permission setAction(String action) {
        this.action = action;
        return this;
    }

    @Override
    public void validate() throws ValidationException {
        if (name.isBlank())
            throw new ValidationException("Permission name can't be blank.");
        if (action.isBlank())
            throw new ValidationException("Permission action can't be blank.");
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null) 
            return false;
        
        if (o instanceof Permission) {
            Permission p = (Permission) o;
            return (
                    p.getAction().equals(action)
                    && p.getName().equals(name)
                );
        }
        
        return false;
    } 

    @Override
    public String toString() {
        return String.format("%s;%s", action, name);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public int compareTo(Permission arg0) {
        return toString().compareTo(arg0.toString());
    }
}
