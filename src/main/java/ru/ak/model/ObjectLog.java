package ru.ak.model;

/**
 * @author a.kakushin
 */
public class ObjectLog {

    private Long id;
    private String name;

    public ObjectLog() {}

    public ObjectLog(String name) {
        this.name = name;
    }

    public ObjectLog(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
