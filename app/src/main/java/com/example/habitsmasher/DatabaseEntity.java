package com.example.habitsmasher;

/**
 * This is the DatabaseEntity abstract class
 * Its purpose is to store the ID's of classes that extend it
 */
public abstract class DatabaseEntity {
    private long _id;

    DatabaseEntity(){}

    DatabaseEntity(long id) {
        _id = id;
    }

    private void setId(long id) {
        _id = id;
    }

    public long getId() {
        return _id;
    }
}
