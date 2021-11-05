package com.example.habitsmasher;

/**
 * This is the DatabaseEntity abstract class
 * Its purpose is to store the ID's of classes that extend it
 */
public abstract class DatabaseEntity {
    private long _id;

    /**
     * Empty constructor
     */
    DatabaseEntity(){}

    /**
     * Constructor for database entity
     * @param id id of database entity
     */
    DatabaseEntity(long id) {
        _id = id;
    }

    /**
     * Sets the ID of the database entity
     * @param id new ID of the entity
     */
    private void setId(long id) {
        _id = id;
    }

    /**
     * Gets the ID of the database entity
     * @return the ID of the entity
     */
    public long getId() {
        return _id;
    }
}
