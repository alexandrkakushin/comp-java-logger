package ru.ak.logger;

import ru.ak.logger.db.*;
import ru.ak.logger.db.connection.DbConnection;
import ru.ak.logger.db.connection.SqliteConnection;
import ru.ak.logger.db.dao.LevelController;
import ru.ak.logger.db.dao.MessageController;
import ru.ak.logger.db.dao.ObjectLogController;
import ru.ak.model.*;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
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

    private HashMap<DbConnection, LoggerDataSource> gates = new HashMap<>();

    private LoggerDataSource getDataSource(DbConnection dbConnection) {
        return this.gates.computeIfAbsent(dbConnection, key -> new LoggerDataSource(dbConnection));
    }

    // Интерфейс взаимодействия

    @WebMethod(operationName = "logs")
    public List<String> logs() {
        return this.gates.keySet().stream().filter(item -> item instanceof SqliteConnection)
                .map(item -> ((SqliteConnection) item).getFileName()).collect(Collectors.toList());
    }

    @WebMethod(operationName = "messagesByPeriod")
    public List<Message> messagesByPeriod(@WebParam(name = "connection") SqliteConnection connection,
            @WebParam(name = "from") Date from, @WebParam(name = "to") Date to, @WebParam(name = "limit") int limit,
            @WebParam(name = "offset") int offset

    ) throws SQLException, ParseException {

        LoggerDataSource loggerDataSource = getDataSource(connection);

        MessageController messageDao = new MessageController(loggerDataSource);
        return (List<Message>) messageDao.findByPeriodBetween(from, to, limit, offset);
    }

    @WebMethod(operationName = "clearMessages")
    public Response clearMessages(@WebParam(name = "connection") SqliteConnection connection) {
        Response response = new Response();
        LoggerDataSource loggerDataSource = getDataSource(connection);

        try {
            MessageController messageDao = new MessageController(loggerDataSource);
            messageDao.deleteAll();

            response.setObject("Success");
        } catch (SQLException ex) {
            response.setError(true);
            response.setDescription(ex.getLocalizedMessage());
        }

        return response;
    }

    @WebMethod(operationName = "logSqlite")
    public Response logSqlite(@WebParam(name = "connection") SqliteConnection connection,
            @WebParam(name = "object") String object, @WebParam(name = "text") String text,
            @WebParam(name = "level") String level) {

        Response response = new Response();
        try {
            LoggerDataSource loggerDataSource = getDataSource(connection);
            DbUtility.init(loggerDataSource);

            LevelController levelDao = new LevelController(loggerDataSource);
            ObjectLogController objectLogDao = new ObjectLogController(loggerDataSource);
            MessageController messageDao = new MessageController(loggerDataSource);

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
        } catch (Exception ex) {
            response.setError(true);
            response.setDescription(ex.getLocalizedMessage());
        }
        return response;
    }
}
