package ru.ak.logger.db.dao;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * @author a.kakushin
 */
public interface EntityDao<T, ID extends Serializable> {

    ID create(T entity) throws SQLException;

    T findByName(String name) throws SQLException;

    Iterable<T> findAll() throws SQLException;
}
