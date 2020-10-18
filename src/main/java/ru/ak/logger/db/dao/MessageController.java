package ru.ak.logger.db.dao;

import ru.ak.logger.db.LoggerDataSource;
import ru.ak.model.DbRecords;
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
public class MessageController extends AbstractController<Message, Long> {

    private static final String SQL_CREATE = "INSERT INTO messages (period, id_level, id_object, text) VALUES (?, ?, ?, ?);";
    private static final String SQL_DELETE_ALL = "DELETE FROM messages";

    static final String FIELD_ID = "id";
    static final String FIELD_PERIOD = "period";
    static final String FIELD_ID_OBJECT = "id_object";
    static final String FIELD_NAME_OBJECT = "name_object";
    static final String FIELD_ID_LEVEL = "id_level";
    static final String FIELD_NAME_LEVEL = "name_level";
    static final String FIELD_TEXT = "text";

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public MessageController(LoggerDataSource lds) {
        super(lds);
    }

    @Override
    public Long create(Message object) throws SQLException {

        Long id;

        try (Connection connection = getLoggerDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE)) {
            ps.setString(1, dateFormat.format(new Date()));
            ps.setLong(2, object.getLevel().getId());
            ps.setLong(3, object.getObjectLog().getId());
            ps.setString(4, object.getText());

            ps.executeUpdate();
            id = getLastId(ps.getConnection());
        }

        return id;
    }

    @Override
    public void deleteAll() throws SQLException {
        try (Connection connection = getLoggerDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(SQL_DELETE_ALL)) {
            ps.execute();
        }
    }

    /**
     * Поиск сообщений, период которых попадает в указанный интервал
     * @param from начало периода
     * @param to окончание периода
     * @param limit ограничение выборки данных
     * @param offset смещение выборки
     * @return Найденные сообщения по периоду
     * @throws SQLException Чтение данныз из базы данных
     * @throws ParseException Преобразование текста в дату
     */
    public DbRecords<Message> findByPeriod(Date from, Date to, int limit, int offset) throws SQLException, ParseException {
        String sql = 
            "SELECT\n"
            + " messages.id AS id,\n"
            + "  messages.period AS period,\n"
            + "  messages.id_level AS id_level,\n"
            + "  levels.name AS name_level,\n"
            + "  messages.id_object AS id_object,\n"
            + "  objects.name AS name_object,\n"
            + "  messages.text AS text\n"
            + "FROM messages\n"
            + "  LEFT JOIN objects ON objects.id = id_object\n"
            + "  LEFT JOIN levels ON levels.id = id_level\n"
            + "WHERE messages.period BETWEEN ? and ?\n" 
            + "ORDER BY messages.period\n"
            + "LIMIT ? OFFSET ?;\n";

        List<Message> messages = new ArrayList<>();

        try (Connection connection = getLoggerDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, dateFormat.format(from));
            ps.setString(2, dateFormat.format(to));
            ps.setInt(3, limit);
            ps.setInt(4, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    messages.add(getMessage(rs));
                }
            }
        }

        String sqlCount = 
            "SELECT count(*) as count_records FROM messages WHERE messages.period BETWEEN ? and ?;"; 

        int count = 0;

        try (Connection connection = getLoggerDataSource().getConnection();
            PreparedStatement ps = connection.prepareStatement(sqlCount)) {

            ps.setString(1, dateFormat.format(from));
            ps.setString(2, dateFormat.format(to));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    count = rs.getInt("count_records");
                }
            }
        }

        return new DbRecords<>(messages, count);
    }

    /**
     * Поиск текста в сообщениях, поиск без учета регистра по шаблону LIKE %value%
     * @param text строка поиска
     * @param limit ограничение выборки данных
     * @param offset смещение выборки
     * @return найденные сообщения
     * @throws SQLException Чтение данных из БД
     * @throws ParseException Преобразование текста в дату
     */
    public DbRecords<Message> findByText(String text, int limit, int offset)  throws SQLException, ParseException {
        String sql =
            "SELECT\n"
            + "  messages.id AS id,\n"
            + "  messages.period AS period,\n"
            + "  messages.id_level AS id_level,\n"
            + "  levels.name AS name_level,\n"
            + "  messages.id_object AS id_object,\n"
            + "  objects.name AS name_object,\n"
            + "  messages.text AS text\n"
            + "FROM messages\n"
            + "  LEFT JOIN objects ON objects.id = id_object\n"
            + "  LEFT JOIN levels ON levels.id = id_level\n"
            + "WHERE UPPER(messages.text) LIKE ?\n"
            + "ORDER BY messages.period\n"
            + "LIMIT ? OFFSET ?;\n";

        List<Message> messages = new ArrayList<>();

        try (Connection connection = getLoggerDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, "%" + text.toUpperCase() + "%");
            ps.setInt(2, limit);
            ps.setInt(3, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    messages.add(getMessage(rs));
                }
            }
        }

        String sqlCount = "SELECT count(*) as count_records FROM messages WHERE UPPER(messages.text) LIKE ?;";

        int count = 0;

        try (Connection connection = getLoggerDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlCount)) {

            ps.setString(1, "%" + text.toUpperCase() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    count = rs.getInt("count_records");
                }
            }
        }

        return new DbRecords<>(messages, count);
    }

    /**
     * Создание экземпляра класса Message на основе данных БД
     * @param rs ResultSet
     * @return Message
     * @throws SQLException Чтение данных из БД
     * @throws ParseException Преобразоваие текста в дату
     */
    private Message getMessage(ResultSet rs) throws SQLException, ParseException {
        return
            new Message(
                rs.getLong(FIELD_ID),
                dateFormat.parse(rs.getString(FIELD_PERIOD)),
                new ObjectLog(rs.getLong(FIELD_ID_OBJECT), rs.getString(FIELD_NAME_OBJECT)),
                new Level(rs.getLong(FIELD_ID_LEVEL), rs.getString(FIELD_NAME_LEVEL)),
                rs.getString(FIELD_TEXT));

    }
}
