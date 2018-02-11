package me.divelog.core.service;

import me.divelog.core.grid.SearchContext;
import me.divelog.core.repository.BaseRepository;

import java.io.Serializable;
import java.util.List;

public interface GenericService<E, ID extends Serializable, R extends BaseRepository<E, ID>> {
    /**
     * Save & Update
     *
     * @param entity
     * @return
     */
    E save(E entity);

    /**
     * Delete
     *
     * @param entity
     */
    void delete(E entity);

    /**
     * Delete by id
     *
     * @param id
     */
    void deleteById(ID id);

    /**
     * Get Entity by Proxy
     *
     * @param id
     * @return
     */
    E getOne(ID id);

    /**
     * Find Entity
     *
     * @param id
     * @return
     */
    E findOne(ID id);

    /**
     * Search Dynamic Query
     *
     * @param sc
     * @return
     */
    List<E> search(SearchContext<E> sc);
}
