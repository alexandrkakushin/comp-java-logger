package ru.ak.logger.db;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.ak.logger.db.connection.DbConnection;
import ru.ak.logger.db.connection.SqliteConnection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author a.kakushin
 */
public class LoggerDataSource {

    private BasicDataSource dataSource;

    public LoggerDataSource(DbConnection dbConnection) {
        dataSource = new BasicDataSource();

        if (dbConnection instanceof SqliteConnection) {
            dataSource.setUrl("jdbc:sqlite:" + ((SqliteConnection) dbConnection).getFileName());
            dataSource.setDriverClassName("org.sqlite.JDBC");
        } else {
            throw new IllegalArgumentException("Подключение не поддерживается");
        }      
    }

    public BasicDataSource getBasicDataSource() {
        return this.dataSource;
    }

    public Connection getConnection() throws SQLException {
        return this.getBasicDataSource().getConnection();
    }
}
