package cz.rozek.jan.base_auth_api_framework.services.transactions.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import cz.rozek.jan.base_auth_api_framework.Entity;
import cz.rozek.jan.base_auth_api_framework.services.transactions.interfaces.ITransaction;

public class Transaction<E extends Entity> implements ITransaction<E> {

    protected MongoRepository<E, String> repository;

    @Override
    public ITransaction<E> setRepository(MongoRepository<E, String> repository) {
        this.repository = repository;
        return this;
    }

    @Override
    public List<E> readAll(E example, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
        Page<E> page = repository.findAll(Example.of(example, matcher), pageable);

        return page.toList();
    }

    @Override
    public List<E> readAll(E example) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
        return repository.findAll(Example.of(example, matcher));
    }

    @Override
    public E readById(String id) {
        Optional<E> entity = repository.findById(id);

        if (entity.isEmpty()) {
            throw new NullPointerException();
        }

        if (entity.get().isEnabled()) {
            return entity.get();
        }

        throw new NullPointerException();
    }

    @Override
    public E readByExample(E example) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
        Optional<E> entity = repository.findOne(Example.of(example, matcher));

        if (entity.isEmpty()) {
            throw new NullPointerException();
        }

        if (entity.get().isEnabled()) {
            return entity.get();
        }

        throw new NullPointerException();
    }

    @Override
    public E create(E entity) {
        entity.validate();

        E saved = repository.save(entity);

        return saved;
    }

    @Override
    public List<E> createMany(List<E> enties) {
        for (E e : enties) {
            e.validate();
        }

        List<E> saved = repository.saveAll(enties);

        return saved;
    }

    @Override
    public E update(String id, E entity) {
        entity.validate();

        if (repository.findById(id).isEmpty()) 
            throw new NullPointerException();

        entity.setId(id);

        repository.save(entity);
        E data = repository.findById(id).get();

        return data;
    }
    @Override
    public List<E> updateMany(Map<String, E> enties) {
        for (String id : enties.keySet()) {
            enties.get(id).validate();
                 
            if (repository.findById(id).isEmpty()) 
                throw new NullPointerException();
            
            enties.get(id).setId(id);
        }

        return repository.saveAll(enties.values());
    }

    @Override
    public E delete(String id) {
        Optional<E> entity = repository.findById(id);

        if (entity.isPresent()) {
            return update(id, entity.get());
        }

        return null;
    }
    
    @Override
    public List<E> deleteByExample(E example, int limit) {
        List<E> toDelete = readAll(example, PageRequest.ofSize(limit));

        for (E e : toDelete) {
            e.setEnabled(false);
        }

        return repository.saveAll(toDelete);
    }
}
