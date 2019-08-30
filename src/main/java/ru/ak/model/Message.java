package ru.ak.model;

import javax.xml.bind.annotation.XmlElement;

import java.util.Date;

/**
 * @author a.kakushin
 */
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

    @XmlElement
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlElement    
    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    @XmlElement    
    public ObjectLog getObjectLog() {
        return objectLog;
    }

    public void setObjectLog(ObjectLog objectLog) {
        this.objectLog = objectLog;
    }

    @XmlElement    
    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    @XmlElement    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}