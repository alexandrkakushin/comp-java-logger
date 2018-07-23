package ru.ak.logger.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author a.kakushin
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Message {

    private Long id;
    private Date period;
    private ObjectLog objectLog;
    private Level level;
    private String text;

    public Message() {}

    public Message(ObjectLog objectLog, Level level, String text) {
        this.objectLog = objectLog;
        this.level = level;
        this.text = text;
    }

    public Message(Long id, ObjectLog objectLog, Level level, String text) {
        this.id = id;
        this.objectLog = objectLog;
        this.level = level;
        this.text = text;
    }

    public Message(Long id, Date period, ObjectLog objectLog, Level level, String text) {
        this.id = id;
        this.period = period;
        this.objectLog = objectLog;
        this.level = level;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    public ObjectLog getObjectLog() {
        return objectLog;
    }

    public void setObjectLog(ObjectLog objectLog) {
        this.objectLog = objectLog;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
