package ru.ak.logger.db.dao;

import ru.ak.logger.db.LoggerDataSource;
import ru.ak.model.Level;
import ru.ak.model.Message;
import ru.ak.model.ObjectLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author a.kakushin
 */
public class MessageDao extends AbstractSqliteDao<Message, Long> {

    private LoggerDataSource loggerDataSource;

    public MessageDao(LoggerDataSource loggerDataSource) {
        super(loggerDataSource);
        this.loggerDataSource = loggerDataSource;
    }

    private static final SimpleDateFormat SQLITE_DATETIME_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected PreparedStatement preparedStatementCreate(Connection connection, Message object) throws SQLException {
        String sql =
            "INSERT INTO messages (period, id_level, id_object, text) " +
            "VALUES (?, ?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, SQLITE_DATETIME_FORMAT.format(new Date()));
        statement.setLong(2, object.getLevel().getId());
        statement.setLong(3, object.getObjectLog().getId());
        statement.setString(4, object.getText());

        return statement;
    }

    @Override
    protected PreparedStatement preparedStatementClear(Connection connection) throws SQLException {
        String sql = "DELETE FROM messages";
        PreparedStatement statement = connection.prepareStatement(sql);

        return statement;
    }

    public Iterable<Message> findByPeriodBetween(Date from, Date to) throws SQLException, ParseException {
        String sql =
            "SELECT\n" +
            "  messages.id AS id,\n" +
            "  messages.period AS period,\n" +
            "  messages.id_level AS id_level,\n" +
            "  levels.name AS name_level,\n" +
            "  messages.id_object AS id_object,\n" +
            "  objects.name AS name_object,\n" +
            "  messages.text AS text\n" +
            "\n" +
            "FROM messages\n" +
            "  LEFT JOIN objects ON objects.id = id_object\n" +
            "  LEFT JOIN levels ON levels.id = id_level\n" +
            "WHERE messages.period BETWEEN ? and ?\n" +
            "ORDER BY messages.period;";

        try (Connection connection = loggerDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, SQLITE_DATETIME_FORMAT.format(from));
            statement.setString(2, SQLITE_DATETIME_FORMAT.format(to));

            List<Message> messages = new ArrayList<>();
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                messages.add(
                    new Message(
                        rs.getLong("id"),
                        SQLITE_DATETIME_FORMAT.parse(rs.getString("period")),
                        new ObjectLog(
                                rs.getLong("id_object"),
                                rs.getString("name_object")),
                        new Level(
                                rs.getLong("id_level"),
                                rs.getString("name_level")),
                        rs.getString("text")
                    )
                );
            }
            return messages;
        }
    }
}
