package ru.ak.logger.db.dao;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * @author a.kakushin
 */
public interface EntityDao<T, Id extends Serializable> {

    T create() throws SQLException;

    void update(T entiry) throws SQLException;

    void delete(T entity) throws SQLException;

    T getById(Id id) throws SQLException;

    Iterable<T> findAll() throws SQLException;
}
