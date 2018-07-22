package ru.ak.logger.db;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.ak.logger.MainClass;
import ru.ak.logger.db.connection.DbConnection;
import ru.ak.logger.db.connection.SqliteConnection;

import java.util.logging.Logger;

/**
 * @author a.kakushin
 */
public class LoggerDataSource {

    private Logger logger =  MainClass.getInstanceLogger();

    private BasicDataSource dataSource;

    public LoggerDataSource(DbConnection dbConnection) {
        dataSource = new BasicDataSource();

        if (dbConnection instanceof SqliteConnection) {
            dataSource.setUrl("jdbc:sqlite:" + ((SqliteConnection) dbConnection).getFileName());
            dataSource.setDriverClassName("org.sqlite.JDBC");
        } else {
            throw new IllegalArgumentException("Подключение не поддерживается");
        }

        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxOpenPreparedStatements(100);
    }

    public BasicDataSource getBasicDataSource() {
        return this.dataSource;
    }
}
