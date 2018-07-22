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
import java.util.HashMap;


/**
 * Web-сервис для реализации логгера
 * @author akakushin
 */
@WebService(name = "Logger", serviceName = "Logger", portName = "LoggerPort")
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

    @WebMethod
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
                levelDb = levelDao.create(new Level(level));
            }

            // Подготовка объекта
            ObjectLog objectLogDb = objectLogDao.findByName(object);
            if (objectLogDb == null) {
                objectLogDb = objectLogDao.create(new ObjectLog(object));
            }

            messageDao.create(new Message(objectLogDb, levelDb, text));

            response.setResult("Success");
        }  catch (Exception ex) {
            response.setError(true);
            response.setDescription(ex.getLocalizedMessage());
        }
        return response;
    }

    @WebMethod
    public Response logH2() {
        Response response = new Response();
        try {
            response.setResult(true);
        }  catch (Exception ex) {
            response.setError(true);
            response.setDescription(ex.getLocalizedMessage());
        }
        return response;
    }
}
