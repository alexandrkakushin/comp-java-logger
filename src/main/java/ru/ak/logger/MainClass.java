
package ru.ak.logger;

import ru.ak.info.InfoService;

import javax.xml.ws.Endpoint;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author akakushin
 */
public class MainClass {

    private static Logger logger;
    private static final ResourceBundle config = ResourceBundle.getBundle("config");

    public static Logger getInstanceLogger() {
        if (logger == null) {
            logger = Logger.getLogger("ru.ak.logger");
        }
        return logger;
    }

    public static void main(String[] args) {

        if (System.getProperty("java.util.logging.config.class") == null
                && System.getProperty("java.util.logging.config.file") == null) {

            logger = getInstanceLogger();
            try {
                if (!existLogDir()) {
                    createLogDir();
                }
                logger.setLevel(Level.ALL);
                final int LOG_ROTATION_COUNT = 10;
                Handler handler = new FileHandler("%h/logs/logger/logger.log", 0, LOG_ROTATION_COUNT);
                logger.addHandler(handler);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Can not create log file handler", ex);
            }
        }

        String port = (args.length == 2 && args[0].equalsIgnoreCase("-p")) ? args[1] : config.getString("port");

        StringBuilder sbUriInfo = new StringBuilder();
        sbUriInfo.append("http://0.0.0.0:").append(port).append("/Info");

        try {
            Endpoint.publish(sbUriInfo.toString(), new InfoService());
            logger.log(Level.INFO, "Info : ok; port: {0}", port);

        } catch (Exception ex) {
            logger.log(Level.WARNING, "Error; {0}", ex.getLocalizedMessage());
        }

    }

    private static boolean createLogDir() {
        String userHome = System.getProperty("user.home");
        File logDir = new File(userHome + "/logs/logger");

        return logDir.mkdirs();
    }

    private static boolean existLogDir() {
        File logDir = new File(System.getProperty("user.home") + "/logs/logger/");
        return logDir.isDirectory();
    }
}
