package ru.ak.logger.db.dao;

import ru.ak.logger.model.InfoBase;

import java.sql.SQLException;

/**
 * @author a.kakushin
 */
public class JdbcInfoBaseDao implements EntityDao<InfoBase, Long> {

    @Override
    public InfoBase create() throws SQLException {
        return null;
    }

    @Override
    public void update(InfoBase entity) throws SQLException {

    }

    @Override
    public void delete(InfoBase entity) throws SQLException {

    }

    @Override
    public InfoBase getById(Long aLong) throws SQLException {
        return null;
    }

    @Override
    public Iterable<InfoBase> findAll() throws SQLException {
        return null;
    }
}
