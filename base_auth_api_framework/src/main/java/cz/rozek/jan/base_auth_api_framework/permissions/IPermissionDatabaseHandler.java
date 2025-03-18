package cz.rozek.jan.base_auth_api_framework.permissions;

import java.util.List;

@FunctionalInterface
public interface IPermissionDatabaseHandler {
    void store(List<Permission> permissions);
}
