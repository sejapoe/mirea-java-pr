package ru.sejapoe.javapr.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {
    default Class<T> getEntityClass() {
        Type[] interfaces = getClass().getInterfaces();

        for (Type t : interfaces) {
            if (t instanceof Class<?>) {
                Class<?> clazz = (Class<?>) t;

                if (clazz.getPackage().getName().startsWith("ru.sejapoe")) {

                    // Repositories should implement only ONE interface from application packages

                    Type genericInterface = clazz.getGenericInterfaces()[0];

                    return (Class<T>) ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
                }
            }
        }

        return null;
    }
}
