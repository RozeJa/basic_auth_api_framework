package cz.rozek.jan.base_auth_api_framework.services.transactions.interfaces;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import cz.rozek.jan.base_auth_api_framework.Entity;

public interface ITransaction<E extends Entity> {
    
    ITransaction<E> setRepository(MongoRepository<E,String> repository);

    List<E> readAll(E example, Pageable pageable);
    List<E> readAll(E example);
    E readById(String id);
    E readByExample(E example);
    E create(E entity);
    List<E> createMany(List<E> enties);
    E update(String id, E entity);
    List<E> updateMany(Map<String, E> enties);
    E delete(String id);
    List<E> deleteByExample(E example, int limit);
}
