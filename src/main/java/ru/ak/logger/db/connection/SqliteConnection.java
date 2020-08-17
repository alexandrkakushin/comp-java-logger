package ru.ak.logger.db.connection;

/**
 * @author a.kakushin
 */
public class SqliteConnection implements DbConnection {

    private String fileName;

    public SqliteConnection() {}

    public SqliteConnection(final String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final SqliteConnection that = (SqliteConnection) o;

        return fileName != null ? fileName.equals(that.fileName) : that.fileName == null;
    }

    @Override
    public int hashCode() {
        return fileName != null ? fileName.hashCode() : 0;
    }
}
