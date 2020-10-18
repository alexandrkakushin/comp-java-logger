package ru.ak.logger;

import ru.ak.logger.db.DbUtility;
import ru.ak.logger.db.LoggerDataSource;
import ru.ak.logger.db.connection.DbConnection;
import ru.ak.logger.db.connection.SqliteConnection;
import ru.ak.logger.db.dao.LevelController;
import ru.ak.logger.db.dao.MessageController;
import ru.ak.logger.db.dao.ObjectLogController;
import ru.ak.model.DbRecords;
import ru.ak.model.Level;
import ru.ak.model.Message;
import ru.ak.model.ObjectLog;
import ru.ak.model.Response;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Web-сервис для реализации логгера
 *
 * @author a.kakushin
 */
@WebService
public class UniLogger {

    private final HashMap<DbConnection, LoggerDataSource> gates = new HashMap<>();

    private LoggerDataSource prepareDataSource(final DbConnection dbConnection) throws SQLException {

        if (dbConnection instanceof SqliteConnection) {
            if (((SqliteConnection) dbConnection).getFileName() == null) {
                throw new SQLException("Can not connected to database. Check parameters");
            }
    
            final Path db = Paths.get(((SqliteConnection) dbConnection).getFileName());
            if (!Files.exists(db)) {
                this.gates.remove(dbConnection);
            }
        }

        LoggerDataSource lds = this.gates.computeIfAbsent(dbConnection, key -> new LoggerDataSource(dbConnection));

        DbUtility.init(lds);

        return lds;
    }

    // Интерфейс взаимодействия

    @WebMethod(operationName = "logs")
    public List<String> logs() {
        return this.gates.keySet().stream().filter(item -> item instanceof SqliteConnection)
                .map(item -> ((SqliteConnection) item).getFileName()).collect(Collectors.toList());
    }

    @WebMethod(operationName = "messagesByPeriod")
    public DbRecords<Message> messagesByPeriod(
            @WebParam(name = "connection") SqliteConnection connection,
            @WebParam(name = "from") Date from,
            @WebParam(name = "to") Date to,
            @WebParam(name = "limit") int limit,
            @WebParam(name = "offset") int offset
    ) throws SQLException, ParseException {

        final LoggerDataSource loggerDataSource = prepareDataSource(connection);

        final MessageController messageDao = new MessageController(loggerDataSource);
        return messageDao.findByPeriod(from, to, limit, offset);
    }

    @WebMethod(operationName = "messagesByText")
    public DbRecords<Message> messagesByText(
            @WebParam(name = "connection") SqliteConnection connection,
            @WebParam(name = "text") String text,
            @WebParam(name = "limit") int limit,
            @WebParam(name = "offset") int offset
    ) throws SQLException, ParseException {

        MessageController messageDao = new MessageController(prepareDataSource(connection));
        return messageDao.findByText(text, limit, offset);
    }

    @WebMethod(operationName = "clearMessages")
    public Response clearMessages(@WebParam(name = "connection") final SqliteConnection connection) {
        final Response response = new Response();

        try {
            final LoggerDataSource loggerDataSource = prepareDataSource(connection);
            final MessageController messageDao = new MessageController(loggerDataSource);
            messageDao.deleteAll();

            response.setObject("Success");
        } catch (final SQLException ex) {
            response.setError(true);
            response.setDescription(ex.getLocalizedMessage());
        }

        return response;
    }

    @WebMethod(operationName = "logSqlite")
    public Response logSqlite(@WebParam(name = "connection") final SqliteConnection connection,
            @WebParam(name = "object") final String object, @WebParam(name = "text") final String text,
            @WebParam(name = "level") final String level) {

        final Response response = new Response();
        try {
            final LoggerDataSource loggerDataSource = prepareDataSource(connection);

            final LevelController levelDao = new LevelController(loggerDataSource);
            final ObjectLogController objectLogDao = new ObjectLogController(loggerDataSource);
            final MessageController messageDao = new MessageController(loggerDataSource);

            // Подготовка уровня
            Level levelDb = levelDao.findByName(level);
            if (levelDb == null) {
                levelDb = new Level(level);
                levelDb.setId(levelDao.create(levelDb));
            }

            // Подготовка объекта
            ObjectLog objectLogDb = objectLogDao.findByName(object);
            if (objectLogDb == null) {
                objectLogDb = new ObjectLog(object);
                objectLogDb.setId(objectLogDao.create(objectLogDb));
            }

            messageDao.create(new Message(objectLogDb, levelDb, text));

            response.setObject("Success");
        } catch (final Exception ex) {
            response.setError(true);
            response.setDescription(ex.getLocalizedMessage());
        }
        return response;
    }
}
