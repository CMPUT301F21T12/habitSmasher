package com.example.habitsmasher;

import android.net.Uri;

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
    private Uri _pictureUri;
    private UUID _id = UUID.randomUUID();

    // TODO: Eventually add location

    /**
     * Empty constructor
     */
    public HabitEvent() {
        _startDate = new Date();
        _comment = "";
        // TODO: Set default image
    }

    /**
     * Default constructor, creates new HabitEvent
     * @param startDate (Date): The start date of the habit event
     * @param comment (String): The comment of the habit event
     * @param pictureUri (String): The URL of the picture of the habit event
     */
    public HabitEvent(Date startDate, String comment, Uri pictureUri) {
        _startDate = startDate;
        _comment = comment;
        _pictureUri = pictureUri;
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
    public Uri getPictureUri() {
        return _pictureUri;
    }

    /**
     * Sets the URL of the picture of a habit event
     * @param pictureUri (Uri): The URL of the picture of the habit event
     */
    public void setPictureURL(Uri pictureUri) {
        this._pictureUri = pictureUri;
    }

    @PropertyName("id")
    public UUID getId() { return _id; }
}
