package cz.rozek.jan.base_auth_api_framework.services.transactions;

import org.springframework.data.mongodb.repository.MongoRepository;

import cz.rozek.jan.base_auth_api_framework.Entity;
import cz.rozek.jan.base_auth_api_framework.permissions.PermissionBuilder;
import cz.rozek.jan.base_auth_api_framework.services.ISecuredService;
import cz.rozek.jan.base_auth_api_framework.services.transactions.impl.SecuredTransaction;
import cz.rozek.jan.base_auth_api_framework.services.transactions.impl.Transaction;
import cz.rozek.jan.base_auth_api_framework.services.transactions.interfaces.ISecuredTransaction;
import cz.rozek.jan.base_auth_api_framework.services.transactions.interfaces.ITransaction;

public abstract class CrudTransactionService<E extends Entity, R extends MongoRepository<E, String>> implements ICrudTransactionService<E, R> {

    protected PermissionBuilder permissionBuilder;
    protected R repository;
    protected ISecuredService securedService;

    public ISecuredService getSecuredService() {
        return securedService;
    }

    @Override
    public ITransaction<E> createTransaction() {
        return new Transaction<E>().setRepository(repository);
    }

    @Override
    public ISecuredTransaction<E> createSecuredTransaction() {
        return new SecuredTransaction<E>()
            .setCreatePermission(createPermission())
            .setReadPermission(readPermission())
            .setUpdatePermission(updatePermission())
            .setDeletePermission(deletePermission())
            .setISecuredService(securedService)
            .setTransaction(createTransaction());
    }

}
