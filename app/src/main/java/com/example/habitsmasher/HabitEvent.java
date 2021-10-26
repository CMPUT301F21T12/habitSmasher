package com.example.habitsmasher;

import com.google.firebase.firestore.PropertyName;

import java.util.Date;
import java.util.UUID;

/**
 * This is the Habit Event class
 * Its purpose is to store and retrieve the start date, comments, picture, and location of a habit event
 */
public class HabitEvent {
    private Date _startDate;
    private String _comment;
    private String _pictureURL;
    private UUID _id = UUID.randomUUID();

    // TODO: Eventually add location

    /**
     * Empty constructor
     */
    public HabitEvent() {
        this._startDate = new Date();
        this._comment = "";
        this._pictureURL = "";
    }

    /**
     * Default constructor, creates new HabitEvent
     * @param startDate (Date): The start date of the habit event
     * @param comment (String): The comment of the habit event
     * @param pictureURL (String): The URL of the picture of the habit event
     */
    public HabitEvent(Date startDate, String comment, String pictureURL) {
        this._startDate = startDate;
        this._comment = comment;
        this._pictureURL = pictureURL;
    }

    /**
     * Gets the start date of a habit event
     * @return startDate: The start date of a habit event
     */
    @PropertyName("startDate")
    public Date getStartDate() {
        return _startDate;
    }

    /**
     * Sets the start date of a habit event
     * @param startDate (Date): The new start date
     */
    public void setStartDate(Date startDate) {
        this._startDate = startDate;
    }

    /**
     * Gets the comment of a habit event
     * @return comment: The comment of a habit event
     */
    @PropertyName("comment")
    public String getComment() {
        return _comment;
    }

    /**
     * Sets the comment of a habit event
     * @param comment (String): The new comment of the habit event
     */
    public void setComment(String comment) {
        this._comment = comment;
    }

    /**
     * Gets the picture URL of a habit event
     * @return pictureURL: The URL of the picture of the habit event
     */
    @PropertyName("picture")
    public String getPictureURL() {
        return _pictureURL;
    }

    /**
     * Sets the URL of the picture of a habit event
     * @param pictureURL (String): The URL of the picture of the habit event
     */
    public void setPictureURL(String pictureURL) {
        this._pictureURL = pictureURL;
    }

    @PropertyName("id")
    public UUID getId() { return _id; }
}
