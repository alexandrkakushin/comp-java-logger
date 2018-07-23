package ru.ak.logger.db.connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author a.kakushin
 */
public class SqliteConnection implements DbConnection {

    private String fileName;

    public SqliteConnection() {}

    public SqliteConnection(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SqliteConnection that = (SqliteConnection) o;

        return fileName != null ? fileName.equals(that.fileName) : that.fileName == null;
    }

    @Override
    public int hashCode() {
        return fileName != null ? fileName.hashCode() : 0;
    }

    public static Long getLastRowId(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet generatedKeys = statement.executeQuery("SELECT last_insert_rowid()");
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            }
        }
        return null;
    }
}
