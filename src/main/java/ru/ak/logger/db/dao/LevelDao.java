package ru.ak.logger.db.dao;

import ru.ak.logger.db.LoggerDataSource;
import ru.ak.logger.model.Level;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author a.kakushin
 */
public class LevelDao extends AbstractSqliteDao<Level, Long> {

    public LevelDao(LoggerDataSource loggerDataSource) {
        super(loggerDataSource);
    }

    @Override
    protected PreparedStatement preparedStatementCreate(Connection connection, Level object) throws SQLException {
        String sql = "INSERT INTO levels (name) VALUES (?);";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, object.getName());

        return statement;
    }

    @Override
    protected PreparedStatement preparedStatementFindByName(Connection connection, String name) throws SQLException {
        String sql = "SELECT id, name FROM levels WHERE name = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);

        return statement;
    }

    @Override
    protected PreparedStatement preparedStatementFindAll(Connection connection) throws SQLException {
        String sql = "SELECT id, name FROM levels;";
        PreparedStatement statement = connection.prepareStatement(sql);

        return statement;
    }

    @Override
    protected PreparedStatement preparedStatementClear(Connection connection) throws SQLException {
        String sql = "DELETE FROM levels";
        PreparedStatement statement = connection.prepareStatement(sql);

        return statement;
    }

    @Override
    protected Level parseObjectDb(ResultSet resultSet)  throws SQLException{
        return new Level(
                resultSet.getLong("id"),
                resultSet.getString("name")
        );
    }

}
