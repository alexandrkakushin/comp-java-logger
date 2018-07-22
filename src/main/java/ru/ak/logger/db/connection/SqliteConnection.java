package ru.ak.logger.db.connection;

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
}
