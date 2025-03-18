package cz.rozek.jan.base_auth_api_framework.services.transactions;

import org.springframework.data.mongodb.repository.MongoRepository;

import cz.rozek.jan.base_auth_api_framework.Entity;
import cz.rozek.jan.base_auth_api_framework.permissions.Permission;
import cz.rozek.jan.base_auth_api_framework.permissions.PermissionBuilder;
import cz.rozek.jan.base_auth_api_framework.services.ISecuredService;
import cz.rozek.jan.base_auth_api_framework.services.transactions.interfaces.ISecuredTransaction;
import cz.rozek.jan.base_auth_api_framework.services.transactions.interfaces.ITransaction;

public interface ICrudTransactionService<E extends Entity, R extends MongoRepository<E, String>> {
    
    void setRepository(R repository);
    void setISecuredService(ISecuredService securedService);

    ITransaction<E> createTransaction();
    ISecuredTransaction<E> createSecuredTransaction();

    Permission createPermission();
    Permission readPermission();
    Permission updatePermission();
    Permission deletePermission();

    void setPermissionBuilder(PermissionBuilder permissionBuilder);

}
