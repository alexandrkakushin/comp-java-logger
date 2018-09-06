package ru.ak.logger.db.dao;

import ru.ak.logger.db.LoggerDataSource;
import ru.ak.logger.model.ObjectLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author a.kakushin
 */
public class ObjectLogDao extends AbstractSqliteDao<ObjectLog, Long> {

    public ObjectLogDao(LoggerDataSource loggerDataSource) {
        super(loggerDataSource);
    }

    @Override
    protected PreparedStatement preparedStatementCreate(Connection connection, ObjectLog object) throws SQLException {
        String sql = "INSERT INTO objects (name) VALUES (?);";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, object.getName());

        return statement;
    }

    @Override
    protected PreparedStatement preparedStatementFindByName(Connection connection, String name) throws SQLException {
        String sql = "SELECT id, name FROM objects WHERE name = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);

        return statement;
    }

    @Override
    protected PreparedStatement preparedStatementFindAll(Connection connection) throws SQLException {
        String sql = "SELECT id, name FROM objects;";
        PreparedStatement statement = connection.prepareStatement(sql);

        return statement;
    }

    @Override
    protected PreparedStatement preparedStatementClear(Connection connection) throws SQLException {
        String sql = "DELETE FROM objects";
        PreparedStatement statement = connection.prepareStatement(sql);

        return statement;
    }

    @Override
    protected ObjectLog parseObjectDb(ResultSet resultSet)  throws SQLException{
        return new ObjectLog(
                resultSet.getLong("id"),
                resultSet.getString("name")
        );
    }
}
