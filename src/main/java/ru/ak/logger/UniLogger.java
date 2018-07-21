package ru.ak.logger;

import ru.ak.logger.db.DbUtility;
import ru.ak.logger.db.LoggerDataSource;
import ru.ak.logger.db.connection.DbConnection;
import ru.ak.logger.db.connection.PostgreDbConnection;
import ru.ak.logger.db.connection.SqliteConnection;
import ru.ak.logger.model.Response;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;


/**
 * Web-сервис для реализации логгера
 * @author akakushin
 */
@WebService(name = "Logger", serviceName = "Logger", portName = "LoggerPort")
public class UniLogger {

    private HashMap<UUID, LoggerDataSource> gates = new HashMap<>();

    private LoggerDataSource getDataSource(UUID uuid) {
        return this.gates.get(uuid);
    }

    private UUID addGate(DbConnection dbConnection) throws SQLException {
        UUID uuid = UUID.randomUUID();
        this.gates.put(uuid, new LoggerDataSource(dbConnection));

        // check and init db
        DbUtility.init(this.getDataSource(uuid));

        return uuid;
    }


    // Интерфейс взаимодействия

    @WebMethod
    public Response connectSqlite(@WebParam(name = "connection") SqliteConnection connection) {
        Response response = new Response();
        try {
            response.setResult(addGate(connection));
        }  catch (Exception ex) {
            response.setError(true);
            response.setDescription(ex.getLocalizedMessage());
        }
        return response;
    }
}
