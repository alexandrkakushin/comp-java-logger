package ru.ak.logger.db.dao;

import ru.ak.logger.db.LoggerDataSource;
import ru.ak.model.Level;
import ru.ak.model.Message;
import ru.ak.model.ObjectLog;

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
public class MessageController extends AbstractController <Message, Long> {
 
    private static final String SQL_CREATE = "INSERT INTO messages (period, id_level, id_object, text) VALUES (?, ?, ?, ?);";
    private static final String SQL_SELECT_ALL = 
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
        "ORDER BY messages.period;";

    private static final String SQL_DELETE_ALL = "DELETE FROM messages";

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public MessageController(LoggerDataSource lds) {
        super(lds);
    }

    @Override
    public Long create(Message object) throws SQLException {
        
        Long id = null;
        
        try (PreparedStatement ps = getPreparedStatement(SQL_CREATE)) {
            ps.setString(1, dateFormat.format(new Date()));
            ps.setLong(2, object.getLevel().getId());
            ps.setLong(3, object.getObjectLog().getId());
            ps.setString(4, object.getText());    

            ps.executeUpdate();
            ps.getConnection().commit();
            id = getLastId(ps.getConnection());
        }
        
        return id;
    }

    @Override
    public Iterable<Message> selectAll() throws SQLException {
        
        List<Message> messages = new ArrayList<>();

        try (PreparedStatement ps = getPreparedStatement(SQL_SELECT_ALL); ResultSet rs = ps.executeQuery()) {            
            while (rs.next()) {
                try {
                    messages.add(
                        new Message(
                            rs.getLong("id"),
                            dateFormat.parse(rs.getString("period")),
                            new ObjectLog(
                                    rs.getLong("id_object"),
                                    rs.getString("name_object")),
                            new Level(
                                    rs.getLong("id_level"),
                                    rs.getString("name_level")),
                            rs.getString("text")
                        )
                    );    
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }
            return messages;
        }
    }

    @Override
    public void deleteAll() throws SQLException {
        try (PreparedStatement ps = getPreparedStatement(SQL_DELETE_ALL)) {
            ps.execute();
            ps.getConnection().commit();
        }
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

        List<Message> messages = new ArrayList<>();

        try (PreparedStatement ps = getPreparedStatement(sql)) {

            ps.setString(1, dateFormat.format(from));
            ps.setString(2, dateFormat.format(to));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    try {
                        messages.add(
                            new Message(
                                rs.getLong("id"),
                                dateFormat.parse(rs.getString("period")),
                                new ObjectLog(
                                        rs.getLong("id_object"),
                                        rs.getString("name_object")),
                                new Level(
                                        rs.getLong("id_level"),
                                        rs.getString("name_level")),
                                rs.getString("text")
                            )
                        );
    
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }    
            }            
        }

        return messages;
    }

}
