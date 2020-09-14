package ru.ak.logger.db.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ru.ak.logger.db.LoggerDataSource;
import ru.ak.model.DbRecords;

/**
 * @author a.kakushin
 */
public abstract class AbstractController<E, K extends Serializable> {

    private LoggerDataSource loggerDataSource;

    AbstractController(LoggerDataSource lds) {
        this.loggerDataSource = lds;
    }

    public static final String SQL_LAST_ID = "SELECT last_insert_rowid()";

    public abstract K create(E object) throws SQLException;

    public abstract DbRecords<E> selectAll() throws SQLException;

    public abstract void deleteAll() throws SQLException;

    public LoggerDataSource getLoggerDataSource() {
        return this.loggerDataSource;
    }

    public Long getLastId(Connection connection) throws SQLException {

        try (Statement statement = connection.createStatement(); ResultSet rs = statement.executeQuery(SQL_LAST_ID)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }

        return null;
    }
}
