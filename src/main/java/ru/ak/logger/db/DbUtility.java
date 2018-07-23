package ru.ak.logger.db;

import ru.ak.logger.MainClass;

import java.sql.*;
import java.util.logging.Logger;

/**
 * @author a.kakushin
 */
public class DbUtility {

    private static Logger logger = MainClass.getInstanceLogger();

    public static void init(LoggerDataSource loggerDataSource) throws SQLException {
        String sqlLevels =
            "CREATE TABLE IF NOT EXISTS levels (" +
            "id integer PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "name text NOT NULL UNIQUE);";

        String sqlObjects =
            "CREATE TABLE IF NOT EXISTS objects (" +
            "id integer PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "name text NOT NULL UNIQUE); ";

        String sqlMessages =
            "CREATE TABLE IF NOT EXISTS messages (" +
            "id integer PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "period text NOT NULL, " +
            "id_level integer NOT NULL, " +
            "id_object integer NOT NULL, " +
            "text text);";

        try (Connection connection = loggerDataSource.getBasicDataSource().getConnection();
             Statement statement = connection.createStatement()) {

            statement.addBatch(sqlLevels);
            statement.addBatch(sqlObjects);
            statement.addBatch(sqlMessages);
            statement.executeBatch();
        } catch (Exception ex) {
            logger.warning("Check tables error; " + ex.getLocalizedMessage());
        }
    }

}
