package ru.ak.logger.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ru.ak.logger.db.LoggerDataSource;
import ru.ak.model.ObjectLog;

/**
 * @author a.kakushin
 */
public class ObjectLogController extends AbstractController<ObjectLog, Long> {

    private static final String SQL_CREATE = "INSERT INTO objects (name) VALUES (?);";
    private static final String SQL_FIND_BY_NAME = "SELECT id, name FROM objects WHERE name = ?;";
    private static final String SQL_SELECT_ALL = "SELECT id, name FROM objects;";
    private static final String SQL_DELETE_ALL = "DELETE FROM objects";

    public ObjectLogController(LoggerDataSource lds) {
        super(lds);
    }

    @Override
    public Long create(ObjectLog object) throws SQLException {
        Long id = null;

        try (Connection connection = getLoggerDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE)) {
            ps.setString(1, object.getName());
            ps.executeUpdate();
            id = getLastId(ps.getConnection());
        }

        return id;
    }

    public ObjectLog findByName(String name) throws SQLException {

        ObjectLog object = null;

        try (Connection connection = getLoggerDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(SQL_FIND_BY_NAME)) {

            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    object = new ObjectLog(rs.getLong("id"), rs.getString("name"));
                }
            }
        }

        return object;
    }

    @Override
    public Iterable<ObjectLog> selectAll() throws SQLException {
        List<ObjectLog> list = new ArrayList<>();
        try (Connection connection = getLoggerDataSource().getConnection();
        PreparedStatement ps = connection.prepareStatement(SQL_SELECT_ALL); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ObjectLog(rs.getLong("id"), rs.getString("name")));
            }
        }

        return list;
    }

    @Override
    public void deleteAll() throws SQLException {
        try (Connection connection = getLoggerDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(SQL_DELETE_ALL)) {
            ps.execute();
            ps.getConnection().commit();
        }
    }
}
