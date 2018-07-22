package ru.ak.logger.model;

/**
 * @author a.kakushin
 */
public class Message {

    private Long id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
