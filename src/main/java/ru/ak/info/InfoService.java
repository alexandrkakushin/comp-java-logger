package ru.ak.info;

import ru.ak.logger.UniLogger;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

/**
 * Корневой Web-сервис, содержащий метод получения версии
 * @author akakushin
 */

@WebService(name = "Info", serviceName = "Info", portName = "InfoPort") 
public class InfoService extends UniLogger {

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
        private List<Build> items;

        public Builds(List<Build> builds) {
            this.items = builds;
        }

        @XmlElement
        public List<Build> getBuilds() {
            return items;
        }
    }
    
    private List<Build> builds() {
        List<Build> builds = new ArrayList<>();
        builds.add(            
            new Build("1.0.0.1", description_1_0_0_1()));

        builds.add(            
            new Build("1.0.0.2", description_1_0_0_2()));

        builds.add(
            new Build("1.0.0.3", description_1_0_0_3()));

        builds.add(
            new Build("1.0.0.4", description_1_0_0_4()));

        builds.add(
            new Build("1.0.0.5", description_1_0_0_5()));
    
        return builds;
    }

    private String description_1_0_0_1() {
        return "Создание проекта";
    }

    private String description_1_0_0_2() {
        return "Миграция на Java 11, рефакторинг проекта";
    }

    private String description_1_0_0_3() {
        return "Исправление ошибки конвертации XDTO-объекта 1С";
    }

    private String description_1_0_0_4() {
        return "Оптимизация работы с базой данных";
    }

    private String description_1_0_0_5() {
        return "Реализовано постраниченое чтение логов";
    }
}