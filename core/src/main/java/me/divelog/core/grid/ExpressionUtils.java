package me.divelog.core.grid;

import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPQLQuery;
import org.hibernate.criterion.MatchMode;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import static org.springframework.util.StringUtils.hasText;

/**
 * QueryDSL 조건 설정용
 */
public class ExpressionUtils {
    public static void like(JPQLQuery query, StringPath path, String value, MatchMode mode) {
        if (hasText(value)) {
            query.where(path.like(mode.toMatchString(value)));
        }
    }

    public static <T> void eq(JPQLQuery query, SimpleExpression<T> path, T value) {
        if (value != null) query.where(path.eq(value));
    }

    public static <T> void eqId(JPQLQuery query, SimpleExpression<Long> path, long id) {
        if (id > 0) query.where(path.eq(id));
    }

    public static void eq(JPQLQuery query, StringPath path, String value) {
        if (hasText(value)) query.where(path.eq(value));
    }

    public static <T extends Enum<T>> void eq(JPQLQuery q, EnumPath<T> path, String value, Class<T> cl) {
        if (!hasText(value)) return;
        eq(q, path, Enum.valueOf(cl, value));
    }

    /**
     * beforeDateTime - 1day
     *
     * @param query
     * @param path
     * @param before
     * @param <T>
     */
    public static <T> void before(JPQLQuery query, DateTimePath path, DateTime before) {
        if (before != null) query.where(path.before(before.plusDays(1)));
    }

    public static <T> void after(JPQLQuery query, DateTimePath path, DateTime after) {
        if (after != null) query.where(path.after(after));
    }

    public static <T> void before(JPQLQuery query, DatePath path, LocalDate before) {
        if (before != null) query.where(path.before(before));
    }

    public static <T> void after(JPQLQuery query, DatePath path, LocalDate after) {
        if (after != null) query.where(path.after(after));
    }
}
