package ru.ak.logger;

import ru.ak.logger.db.DbUtility;
import ru.ak.logger.db.LoggerDataSource;
import ru.ak.logger.db.connection.DbConnection;
import ru.ak.logger.db.connection.SqliteConnection;
import ru.ak.logger.db.dao.LevelDao;
import ru.ak.logger.db.dao.MessageDao;
import ru.ak.logger.db.dao.ObjectLogDao;
import ru.ak.logger.model.Level;
import ru.ak.logger.model.Message;
import ru.ak.logger.model.ObjectLog;
import ru.ak.logger.model.Response;

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
 * @author a.kakushin
 */
@WebService
public class UniLogger {

    private HashMap<DbConnection, LoggerDataSource> gates = new HashMap<>();

    private LoggerDataSource getDataSource(DbConnection dbConnection) {
        LoggerDataSource dataSource = this.gates.get(dbConnection);
        if (dataSource == null) {
            dataSource = new LoggerDataSource(dbConnection);
            this.gates.put(dbConnection, dataSource);
        }
        return dataSource;
    }

    // Интерфейс взаимодействия

    @WebMethod(operationName = "logs")
    public List<String> logs() {
        return
            this.gates.keySet().stream()
                .filter(item -> item instanceof SqliteConnection)
                .map(item -> ((SqliteConnection) item).getFileName())
                .collect(Collectors.toList());
    }

    @WebMethod(operationName = "messagesByPeriod")
    public List<Message> messagesByPeriod(
            @WebParam(name = "connection") SqliteConnection connection,
            @WebParam(name = "from") Date from,
            @WebParam(name = "to") Date to) throws SQLException, ParseException {

        LoggerDataSource loggerDataSource = getDataSource(connection);

        MessageDao messageDao = new MessageDao(loggerDataSource);
        return (List<Message>) messageDao.findByPeriodBetween(from, to);
    }

    @WebMethod(operationName = "logSqlite")
    public Response logSqlite(
            @WebParam(name = "connection") SqliteConnection connection,
            @WebParam(name = "object") String object,
            @WebParam(name = "text") String text,
            @WebParam(name = "level") String level) {

        Response response = new Response();
        try {
            LoggerDataSource loggerDataSource = getDataSource(connection);
            DbUtility.init(loggerDataSource);

            LevelDao levelDao = new LevelDao(loggerDataSource);
            ObjectLogDao objectLogDao = new ObjectLogDao(loggerDataSource);
            MessageDao messageDao = new MessageDao(loggerDataSource);

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
        }  catch (Exception ex) {
            response.setError(true);
            response.setDescription(ex.getLocalizedMessage());
        }
        return response;
    }
}
