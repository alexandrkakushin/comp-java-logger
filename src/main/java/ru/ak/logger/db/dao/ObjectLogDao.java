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
public class ObjectLogDao implements EntityDao<ObjectLog, Long> {

    private LoggerDataSource loggerDataSource;

    public ObjectLogDao(LoggerDataSource loggerDataSource) {
        this.loggerDataSource = loggerDataSource;
    }

    @Override
    public ObjectLog create(ObjectLog objectLog) throws SQLException {
        ObjectLog objectLogDb = new ObjectLog();

        String sqlInsert = "INSERT INTO objects (name) VALUES (?);";
        try (Connection connection = loggerDataSource.getBasicDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlInsert)) {

            statement.setString(1, objectLog.getName());

            objectLogDb.setId(Long.valueOf(statement.executeUpdate()));
            objectLogDb.setName(objectLog.getName());
        }
        return objectLogDb;
    }

    @Override
    public void update(ObjectLog entity) throws SQLException {

    }

    @Override
    public void delete(ObjectLog entity) throws SQLException {

    }

    @Override
    public ObjectLog getById(Long aLong) throws SQLException {
        return null;
    }

    public ObjectLog findByName(String name) throws SQLException {
        String sql = "SELECT id, name FROM objects WHERE name = ?";

        try (Connection connection = loggerDataSource.getBasicDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new ObjectLog(
                        rs.getLong("id"),
                        rs.getString("name"));
            }
        }
        return null;
    }

    @Override
    public Iterable<ObjectLog> findAll() throws SQLException {
        return null;
    }
}
