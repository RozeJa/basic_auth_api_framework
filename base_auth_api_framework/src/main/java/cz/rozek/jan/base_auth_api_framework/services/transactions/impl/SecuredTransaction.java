package cz.rozek.jan.base_auth_api_framework.services.transactions.impl;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import cz.rozek.jan.base_auth_api_framework.Entity;
import cz.rozek.jan.base_auth_api_framework.permissions.Permission;
import cz.rozek.jan.base_auth_api_framework.services.ISecuredService;
import cz.rozek.jan.base_auth_api_framework.services.transactions.interfaces.ISecuredTransaction;
import cz.rozek.jan.base_auth_api_framework.services.transactions.interfaces.ITransaction;

public class SecuredTransaction<E extends Entity> implements ISecuredTransaction<E> {

    protected String jwt;
    protected ITransaction<E> transaction;
    protected ISecuredService securedService;

    protected Permission creadPermission;
    protected Permission readPermission;
    protected Permission updatePermission;
    protected Permission deletePermission;

    @Override
    public ISecuredTransaction<E> setCreatePermission(Permission permission) {
        this.creadPermission = permission;
        return this;
    }
    @Override
    public ISecuredTransaction<E> setReadPermission(Permission permission) {
        this.readPermission = permission;
        return this;
    }
    @Override
    public ISecuredTransaction<E> setUpdatePermission(Permission permission) {
        this.updatePermission = permission;
        return this;
    }
    @Override
    public ISecuredTransaction<E> setDeletePermission(Permission permission) {
        this.deletePermission = permission;
        return this;
    }

    @Override
    public ISecuredTransaction<E> setISecuredService(ISecuredService securedService) {
        this.securedService = securedService;
        return this;
    }
    @Override
    public ISecuredTransaction<E> setJWT(String jwt) {
        this.jwt = jwt;
        return this;
    }
    @Override
    public ISecuredTransaction<E> setTransaction(ITransaction<E> transaction) {
        this.transaction = transaction;
        return this;
    }
    @Override
    public ITransaction<E> setRepository(MongoRepository<E, String> repository) {
        throw new UnsupportedOperationException("Unimplemented method 'setRepository'");
    }
    

    @Override
    public E create(E entity) {
        securedService.verifyAccess(jwt, creadPermission);

        return transaction.create(entity);
    }

    @Override
    public List<E> readAll(E example, Pageable pageable) {
        securedService.verifyAccess(jwt, readPermission);

        return transaction.readAll(example, pageable);
    }

    @Override
    public List<E> readAll(E example) {
        securedService.verifyAccess(jwt, readPermission);

        return transaction.readAll(example);
    }

    @Override
    public E readById(String id) {
        securedService.verifyAccess(jwt, readPermission);

        return transaction.readById(id);
    }

    @Override
    public E readByExample(E example) {
        securedService.verifyAccess(jwt, readPermission);

        return transaction.readByExample(example);
    }

    @Override
    public E update(String id, E entity) {
        securedService.verifyAccess(jwt, updatePermission);

        return transaction.update(id, entity);
    }

    @Override
    public E delete(String id) {
        securedService.verifyAccess(jwt, deletePermission);

        return transaction.delete(id);
    }

    @Override
    public List<E> deleteByExample(E example, int limit) {
        securedService.verifyAccess(jwt, deletePermission);

        return transaction.deleteByExample(example, limit);
    }

}
