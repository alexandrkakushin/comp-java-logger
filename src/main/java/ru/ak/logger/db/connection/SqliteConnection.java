package ru.ak.logger.db.connection;

/**
 * @author a.kakushin
 */
public class SqliteConnection implements DbConnection {

    private String fileName;

    public SqliteConnection() {
    }

    public SqliteConnection(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
