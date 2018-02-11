package me.divelog.core.service;

import me.divelog.core.grid.SearchContext;
import me.divelog.core.repository.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Transactional
public class GenericServiceImpl<E, ID extends Serializable, R extends BaseRepository<E, ID>> implements GenericService<E, ID, R> {
    final protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected R repository;

    @Override
    public E save(E entity) {
        return repository.save(entity);
    }

    @Override
    public void delete(E entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteById(ID id) {
        repository.delete(id);
    }

    @Override
    public E getOne(ID id) {
        return repository.getOne(id);
    }

    @Override
    public E findOne(ID id) {
        return repository.findOne(id);
    }

    @Override
    public List<E> search(SearchContext<E> sc) {
        return repository.search(sc);
    }
}
