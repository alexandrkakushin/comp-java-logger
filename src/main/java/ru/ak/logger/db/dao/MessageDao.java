package ru.ak.logger.db.dao;

import ru.ak.logger.db.LoggerDataSource;
import ru.ak.logger.model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author a.kakushin
 */
public class MessageDao implements EntityDao<Message, Long> {

    private LoggerDataSource loggerDataSource;

    public MessageDao(LoggerDataSource loggerDataSource) {
        this.loggerDataSource = loggerDataSource;
    }

    @Override
    public Message create(Message entity) throws SQLException {
        Message messageDb = new Message();

        String sqlInsert = "INSERT INTO messages (id_level, id_object, text) VALUES (?, ?, ?);";
        try (Connection connection = loggerDataSource.getBasicDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlInsert)) {

            statement.setLong(1, entity.getLevel().getId());
            statement.setLong(2, entity.getObjectLog().getId());
            statement.setString(3, entity.getText());

            messageDb.setId(Long.valueOf(statement.executeUpdate()));
        }
        return messageDb;
    }

    @Override
    public void update(Message entity) throws SQLException {

    }

    @Override
    public void delete(Message entity) throws SQLException {

    }

    @Override
    public Message getById(Long id) throws SQLException {
        return null;
    }

    @Override
    public Iterable<Message> findAll() throws SQLException {
        return null;
    }
}
