package ru.ak.logger.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Утилитный класс для создания таблиц БД
 * 
 * @author a.kakushin
 */
public class DbUtility {

    private DbUtility() {
        throw new IllegalStateException("Utility class");
    }

    private static final String SQL_LEVELS = "CREATE TABLE IF NOT EXISTS levels (id integer PRIMARY KEY AUTOINCREMENT NOT NULL, name text NOT NULL UNIQUE);";

    private static final String SQL_OBJECTS = "CREATE TABLE IF NOT EXISTS objects (id integer PRIMARY KEY AUTOINCREMENT NOT NULL, name text NOT NULL UNIQUE);";

    private static final String SQL_MESSAGES = "CREATE TABLE IF NOT EXISTS messages (id integer PRIMARY KEY AUTOINCREMENT NOT NULL, period text NOT NULL, id_level integer NOT NULL, id_object integer NOT NULL, text text);";

    public static void init(final LoggerDataSource loggerDataSource) throws SQLException {

        try (Connection connection = loggerDataSource.getConnection();
                Statement statement = connection.createStatement()) {
            statement.addBatch(SQL_LEVELS);
            statement.addBatch(SQL_OBJECTS);
            statement.addBatch(SQL_MESSAGES);
            statement.executeBatch();
        }
    }

}
