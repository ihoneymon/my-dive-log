package me.divelog.core.support;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class CommonUtils {
    /**
     * Generic 을 사용한 클래스 clzz 에서 지정된 idx 번째 값을 가져온다
     *
     * @param clazz
     * @param idx
     * @param <T>
     * @return
     */
    public static <T> Class<T> getGenericTypeParam(Class<?> clazz, int idx) {
        ParameterizedType genericSuperclass = (ParameterizedType) clazz.getGenericSuperclass();
        Type type = genericSuperclass.getActualTypeArguments()[idx];

        if (type instanceof ParameterizedType) {
            return (Class<T>) ((ParameterizedType) type).getRawType();
        } else {
            return (Class<T>) type;
        }
    }
}
