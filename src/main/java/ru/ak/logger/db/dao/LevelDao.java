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
public class LevelDao implements EntityDao<Level, Long>{

    private LoggerDataSource loggerDataSource;

    public LevelDao(LoggerDataSource loggerDataSource) {
        this.loggerDataSource = loggerDataSource;
    }

    @Override
    public Level create(Level level) throws SQLException {
        Level levelDb = new Level();

        String sqlInsert = "INSERT INTO levels (name) VALUES (?);";
        try (Connection connection = loggerDataSource.getBasicDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlInsert)) {

            statement.setString(1, level.getName());

            levelDb.setId(Long.valueOf(statement.executeUpdate()));
            levelDb.setName(level.getName());
        }
        return levelDb;
    }

    @Override
    public void update(Level entity) throws SQLException {

    }

    @Override
    public void delete(Level entity) throws SQLException {

    }

    @Override
    public Level getById(Long id) throws SQLException {
        return null;
    }

    public Level findByName(String name) throws SQLException {
        String sql =
            "SELECT id, name FROM levels WHERE name = ?";

        try (Connection connection = loggerDataSource.getBasicDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Level(
                        rs.getLong("id"),
                        rs.getString("name"));
            }
        }
        return null;
    }

    @Override
    public Iterable<Level> findAll() throws SQLException {
        return null;
    }
}
