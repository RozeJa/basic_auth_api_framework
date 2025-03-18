package cz.rozek.jan.base_auth_api_framework.services.transactions.impl;

import java.util.List;

import org.springframework.data.domain.Pageable;

import cz.rozek.jan.base_auth_api_framework.OwnedEntity;

public class OwnedSecuredTransaction<E extends OwnedEntity> extends SecuredTransaction<E> {
    
    @Override
    public E create(E entity) {
        try {

            return super.create(entity);  
        } catch (SecurityException e) {
            String ownerId = findExpextedOwnerId();

            if (entity.getOwnerId().equals(ownerId)) {
                return transaction.create(entity);
            }
    
            throw new SecurityException("User can not create this record because it would not belongs to him.");
        }
    }

    @Override
    public List<E> readAll(E example, Pageable pageable) {
        try {
            return super.readAll(example, pageable);
        } catch (SecurityException e) {
            String ownerId = findExpextedOwnerId();
            
            example.setOwnerId(ownerId);
            return transaction.readAll(example, pageable);
        }
    }

    @Override
    public List<E> readAll(E example) {
        try {
            return super.readAll(example);
        } catch (SecurityException e) {
            String ownerId = findExpextedOwnerId();
            
            example.setOwnerId(ownerId);
            return transaction.readAll(example);
        }
    }

    @Override
    public E readById(String id) {
        try {
            return super.readById(id);
        } catch (SecurityException e) {
            String ownerId = findExpextedOwnerId();

            E data = transaction.readById(id);
            if (data.getOwnerId().equals(ownerId)) {
                return data;
            } 

            throw new SecurityException("User can not read this record because it is not belonging to him.");
        }
    }

    @Override
    public E readByExample(E example) {
        try {            
            return super.readByExample(example);
        } catch (SecurityException e) {
            String ownerId = findExpextedOwnerId();
            
            example.setOwnerId(ownerId);
            return transaction.readByExample(example);
        }
    }

    @Override
    public E update(String id, E entity) {
        try {
            return super.update(id, entity);
        } catch (SecurityException e) {
            String ownerId = findExpextedOwnerId();

            if (entity.getOwnerId().equals(ownerId) && transaction.readById(id).getOwnerId().equals(ownerId)) {
                return transaction.update(id, entity);
            }
    
            throw new SecurityException("User can not update this record because it would not belongs to him or it is not belong to him.");
        }
    }

    @Override
    public E delete(String id) {
        try {
            return super.delete(id);
        } catch (SecurityException e) {
            String ownerId = findExpextedOwnerId();

            if (transaction.readById(id).getOwnerId().equals(ownerId)) {
                return transaction.delete(id);
            }
            
            throw new SecurityException("User can not delete this record because it is not belonging to him.");
        }
    }

    @Override
    public List<E> deleteByExample(E example, int limit) {
        try {
            return super.deleteByExample(example, limit);
        } catch (SecurityException e) {
            String ownerId = findExpextedOwnerId();

            if (example.getOwnerId().equals(ownerId)) {
                transaction.deleteByExample(example, limit);
            }
            
            throw new SecurityException("User can not delete this record because it is not belonging to him.");
        }
    }

    private String findExpextedOwnerId() {
        return securedService.getAuthorizationService().getUserId(jwt);
    }
}
