package cz.rozek.jan.base_auth_api_framework.services.transactions.interfaces;

import cz.rozek.jan.base_auth_api_framework.Entity;
import cz.rozek.jan.base_auth_api_framework.permissions.Permission;
import cz.rozek.jan.base_auth_api_framework.services.ISecuredService;

public interface ISecuredTransaction<E extends Entity> extends ITransaction<E> {
    
    ISecuredTransaction<E> setISecuredService(ISecuredService securedService);
    ISecuredTransaction<E> setJWT(String jwt);
    ISecuredTransaction<E> setTransaction(ITransaction<E> transaction);

    ISecuredTransaction<E> setCreatePermission(Permission permission);
    ISecuredTransaction<E> setReadPermission(Permission permission);
    ISecuredTransaction<E> setUpdatePermission(Permission permission);
    ISecuredTransaction<E> setDeletePermission(Permission permission);
}
