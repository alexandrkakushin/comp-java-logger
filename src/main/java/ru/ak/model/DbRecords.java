package ru.ak.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso({Message.class})
public class DbRecords<E> {
    
    private List<E> items;

    private int count;

    public DbRecords() {}

    public DbRecords(List<E> items) {
        this(items, items.size());
    }

    public DbRecords(List<E> items, int count) {
        this.items = items;
        this.count = count;
    }

    @XmlAnyElement(lax = true)
    public List<E> getItems() {
        return this.items;
    }

    @XmlElement
    public int getCount() {
        return this.count;
    }
}
