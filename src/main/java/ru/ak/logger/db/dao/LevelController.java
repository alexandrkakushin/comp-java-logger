package ru.ak.logger.db.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ru.ak.logger.db.LoggerDataSource;
import ru.ak.model.Level;

/**
 * DAO "LEVELS"
 * 
 * @author a.kakushin
 */
public class LevelController extends AbstractController<Level, Long> {

    private static final String SQL_CREATE = "INSERT INTO levels (name) VALUES (?);";
    private static final String SQL_FIND_BY_NAME = "SELECT id, name FROM levels WHERE name = ?;";
    private static final String SQL_SELECT_ALL = "SELECT id, name FROM levels;";
    private static final String SQL_DELETE_ALL = "DELETE FROM levels";

    public LevelController(LoggerDataSource lds) {
        super(lds);
    }
    
    @Override
    public Long create(Level level) throws SQLException {

        Long id = null;

        try (PreparedStatement ps = getPreparedStatement(SQL_CREATE)) {
            if (ps != null) {
                ps.setString(1, level.getName());
                ps.executeUpdate();
                ps.getConnection().commit();
                id = getLastId(ps.getConnection());
            }
        }

        return id;
    }

    public Level findByName(String name) throws SQLException {

        Level level = null;
        try (PreparedStatement ps = getPreparedStatement(SQL_FIND_BY_NAME)) {
            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    level = new Level(rs.getLong("id"), rs.getString("name"));
                }
            }
        }

        return level;
    }

    @Override
    public Iterable<Level> selectAll() throws SQLException {
        List<Level> list = new ArrayList<>();
        try (PreparedStatement ps = getPreparedStatement(SQL_SELECT_ALL); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Level(rs.getLong("id"), rs.getString("name")));
            }
        }

        return list;
    }

    @Override
    public void deleteAll() throws SQLException {
        try (PreparedStatement ps = getPreparedStatement(SQL_DELETE_ALL)) {
            ps.execute();
            ps.getConnection().commit();
        }
    }

}
