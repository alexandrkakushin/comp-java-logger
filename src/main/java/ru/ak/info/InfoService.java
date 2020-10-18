package ru.ak.info;

import ru.ak.logger.UniLogger;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import java.util.*;

/**
 * Корневой Web-сервис, содержащий метод получения версии
 * @author akakushin
 */

@WebService(name = "Info", serviceName = "Info", portName = "InfoPort") 
public class InfoService extends UniLogger {

    private static final ResourceBundle descriptions = ResourceBundle.getBundle("versions");

    /**
     * Получение версии компоненты
     * @return Версия компоненты 
     */
    @WebMethod(operationName = "version")
    public String version() {
        return builds().isEmpty() ? "null" : builds().get(builds().size() - 1).getVersion();
    }

    /** 
    * Получение списка изменений компоненты
    * @return Список
    */
    @WebMethod(operationName = "details") 
    public Builds details() {
        return new Builds(builds());
    }

    public static class Builds {
        private final List<Build> items;

        public Builds(List<Build> builds) {
            this.items = builds;
        }

        @XmlElement
        public List<Build> getBuilds() {
            return items;
        }
    }
    
    private List<Build> builds() {
        Set<String> versions = new HashSet<>();
        versions.add("1.0.0.1");
        versions.add("1.0.0.2");
        versions.add("1.0.0.3");
        versions.add("1.0.0.4");
        versions.add("1.0.0.5");
        versions.add("1.0.0.6");

        List<Build> builds = new ArrayList<>();

        versions.forEach(
            item -> builds.add(
                new Build(item, descriptions.getString(item))
            )
        );

        return builds;
    }
}