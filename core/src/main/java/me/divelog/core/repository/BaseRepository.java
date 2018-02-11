package me.divelog.core.repository;

import me.divelog.core.grid.SearchContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * @param <E>  Entity
 * @param <ID> ID
 */
@NoRepositoryBean
public interface BaseRepository<E, ID extends Serializable> extends JpaRepository<E, ID> {
    /**
     * {@link SearchContext}를 기준으로 동적 페이징쿼리 실행 후 결과 리턴
     * - 클라이언트에 리턴할 페이징 정보는 ctx 에 남김
     * @param ctx
     * @return
     */
    List<E> search(SearchContext<E> ctx);
}
