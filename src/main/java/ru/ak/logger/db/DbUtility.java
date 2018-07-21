package ru.ak.logger.db;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.ak.logger.MainClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * @author a.kakushin
 */
public class DbUtility {

    private static Logger logger = MainClass.getInstanceLogger();

    public static void init(LoggerDataSource dataSource) throws SQLException {
        // create table "INFOBASES"
        String sql =
            "CREATE TABLE infobases (\n" +
            " id integer PRIMARY KEY,\n" +
            " name text NOT NULL UNIQUE);";

        logger.info("Create table: 'INFOBASES'");
        try (BasicDataSource basicDataSource = dataSource.getBasicDataSource();
             Connection connection = basicDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.execute();

        } catch (Exception ex) {
            logger.warning("Error; " + ex.getLocalizedMessage());
        }
    }
}
