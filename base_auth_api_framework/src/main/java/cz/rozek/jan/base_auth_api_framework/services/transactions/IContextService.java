package cz.rozek.jan.base_auth_api_framework.services.transactions;

import java.util.Collection;

import cz.rozek.jan.base_auth_api_framework.Entity;

public interface IContextService<E extends Entity> {
    E readByContext(String context, String jwt);
    E readByContext(String context, Collection<E> data);
}
