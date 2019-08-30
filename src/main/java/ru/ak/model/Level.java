package ru.ak.model;

/**
 * @author a.kakushin
 */
public class Level implements Cloneable {

    private Long id;
    private String name;

    public Level() {}

    public Level(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Level(String name) {
        this.name = name;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
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
