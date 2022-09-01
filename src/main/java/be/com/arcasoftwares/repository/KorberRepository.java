package be.com.arcasoftwares.repository;

import java.util.List;
import java.util.Optional;

public interface KorberRepository<T> {
    T upsert(T t);

    List<T> findAll();

    Optional<T> findByKey(String key);
}
