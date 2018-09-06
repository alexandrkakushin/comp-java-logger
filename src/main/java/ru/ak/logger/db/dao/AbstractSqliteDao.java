package ru.ak.logger.db.dao;

import ru.ak.logger.db.LoggerDataSource;
import ru.ak.logger.db.connection.SqliteConnection;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author a.kakushin
 */
public abstract class AbstractSqliteDao<T, ID extends Serializable> implements EntityDao<T, ID> {

    private LoggerDataSource loggerDataSource;

    public AbstractSqliteDao(LoggerDataSource loggerDataSource) {
        this.loggerDataSource = loggerDataSource;
    }

    protected abstract PreparedStatement preparedStatementCreate(Connection connection, T object) throws SQLException;

    protected PreparedStatement preparedStatementFindByName(Connection connection, String name) throws SQLException {
        return null;
    }

    protected PreparedStatement preparedStatementFindAll(Connection connection) throws SQLException {
        return null;
    }

    protected PreparedStatement preparedStatementClear(Connection connection) throws SQLException {
        return  null;
    }

    protected T parseObjectDb(ResultSet resultSet) throws SQLException {
        return null;
    }

    @Override
    public ID create(T entity) throws SQLException {
        try (Connection connection = loggerDataSource.getConnection();
             PreparedStatement preparedStatement = preparedStatementCreate(connection, entity)) {

            connection.setAutoCommit(false);
            preparedStatement.executeUpdate();
            connection.commit();

            return (ID) SqliteConnection.getLastRowId(connection);
        }
    }

    @Override
    public T findByName(String name) throws SQLException {
        try (Connection connection = loggerDataSource.getBasicDataSource().getConnection();
             PreparedStatement statement = preparedStatementFindByName(connection, name)) {

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return parseObjectDb(rs);
            }
        }
        return null;
    }

    @Override
    public Iterable<T> findAll() throws SQLException {
        try (Connection connection = loggerDataSource.getBasicDataSource().getConnection();
             PreparedStatement statement = preparedStatementFindAll(connection)) {

            ArrayList<T> result = new ArrayList<>();
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                result.add(parseObjectDb(rs));
            }
            return result;
        }
    }

    @Override
    public void clear() throws SQLException {
        try (Connection connection = loggerDataSource.getBasicDataSource().getConnection();
            PreparedStatement statement = preparedStatementClear(connection)) {
            statement.execute();
        }
    }
}
