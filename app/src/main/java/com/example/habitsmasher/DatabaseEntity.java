package com.example.habitsmasher;


import java.util.UUID;

/**
 * This is the DatabaseEntity abstract class
 * Its purpose is to store the ID's of classes that extend it
 *
 * @author Kaden Dreger, Rudy Patel, Jason Kim
 */
public abstract class DatabaseEntity {

    // IDs will be UUIDs converted into Strings
    private String _id;

    /**
     * Empty constructor
     */
    DatabaseEntity(){}

    /**
     * Constructor for database entity
     * @param id id of database entity
     */
    DatabaseEntity(String id) {
        _id = id;
    }

    /**
     * Sets the ID of the database entity
     * @param id new ID of the entity
     */
    private void setId(String id) {
        _id = id;
    }

    /**
     * Gets the ID of the database entity
     * @return the ID of the entity
     */
    public String getId() {
        return _id;
    }

    /**
     * Generates a random UUID in the form of a string for the
     * database entity
     * @return
     */
    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}
