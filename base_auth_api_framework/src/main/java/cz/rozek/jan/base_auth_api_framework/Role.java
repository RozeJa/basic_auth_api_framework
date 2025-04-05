package cz.rozek.jan.base_auth_api_framework;

import java.util.Set;
import java.util.TreeSet;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import cz.rozek.jan.base_auth_api_framework.exceptions.ValidationException;
import cz.rozek.jan.base_auth_api_framework.permissions.Permission;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("roles")
@Data
@NoArgsConstructor
public class Role implements Comparable<Role>, Entity {
    
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    @DBRef
    private Set<Permission> permissions;
    @DBRef
    private Set<Permission> grantablePermissions;
    
    private boolean autoAddAble = false;
    private boolean enabled = true;

    public void addPermissions(Permission permission) {
        if (permissions == null) {
            permissions = new TreeSet<>();
        }
        permissions.add(permission);
    }
    public Permission removePermission(Permission permission) {
        if (permissions == null) {
            return null;
        }

        return permissions.remove(permission) ? permission : null;
    }

    public void addGrantablePermissions(Permission permission) {
        if (grantablePermissions == null) {
            grantablePermissions = new TreeSet<>();
        }
        grantablePermissions.add(permission);
    }
    public Permission removeGrantablePermissions(Permission permission) {
        if (grantablePermissions == null) {
            return null;
        }

        return grantablePermissions.remove(permission) ? permission : null;
    }
    
    public boolean containsPermission(Permission permission) {
        if (permissions == null) 
            return false;
        return permissions.contains(permission);
    }

    public boolean containsGrantablePermission(Permission permission) {
        if (grantablePermissions == null) 
            return false;
        return grantablePermissions.contains(permission);
    }

    @Override
    public void validate() throws ValidationException {
        if (name.isBlank())
            throw new ValidationException("Role name can't be blank.");
    }

    public void update(Role data) {
        if (!data.name.isBlank()) 
            this.name = data.name;
        this.autoAddAble = data.autoAddAble;
        this.enabled = data.enabled;
    }

    @Override
    public int compareTo(Role role) {
        return name.compareTo(role.getName());
    }

    public String findPermissionsNames() {
        return findPermissionsNames(permissions);
    }
    public String findGrantablePermissionsNames() {
        return findPermissionsNames(grantablePermissions);
    }

    private String findPermissionsNames(Set<Permission> permissions) {
        StringBuilder sb  = new StringBuilder();

        Permission[] arr = (Permission[]) permissions.toArray();

        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i].getName());
            sb.append("-");
            sb.append(arr[i].getAction());

            if (i + 1 < arr.length) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("""
            Role %s 
            have %d permissions: %s 
            and %d grantable permissions: %s
            %s
            %s
            """, name, permissions.size(), findPermissionsNames(), grantablePermissions.size(), findGrantablePermissionsNames(), 
            autoAddAble ? "role is automatically added to new users" : "",
            !enabled ? "role is not enabled" : "" );
    }
}
