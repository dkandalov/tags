package com.greeneyes.tags;

import java.util.List;

/**
 * Author alex at 20.01.11 8:54
 */
public class Item {
    private long id;
    private String name;
    private List<Tag> tags;

    public Item(long id, String name, List<Tag> tags) {
        this.id = id;
        this.name = name;
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Tag> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
