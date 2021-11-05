package com.example.habitsmasher;
import android.net.Uri;
import com.google.firebase.firestore.PropertyName;
import java.util.Date;

/**
 * This is the Habit Event class
 * Its purpose is to store and retrieve the start date, comments, picture, and location of a habit event
 * Pictures for habit events are not yet implemented
 */
public class HabitEvent {
    private Date _date;
    private String _comment;
    private Uri _pictureUri;
    private String _id;

    // TODO: Eventually add location
    // TODO: Fully integrate pictures

    /**
     * Empty constructor
     */
    public HabitEvent() {
        // Needed for firestore
    }

    /**
     * Default constructor, creates new HabitEvent
     * @param startDate (Date): The start date of the habit event
     * @param comment (String): The comment of the habit event
     */
    public HabitEvent(Date startDate, String comment, String id) {
        _date = startDate;
        _comment = comment;
        _id = id;
        // _pictureUri = pictureUri;
    }

    /**
     * Gets the start date of a habit event
     * @return startDate: The start date of a habit event
     */
    @PropertyName("date")
    public Date getDate() {
        return _date;
    }

    /**
     * Sets the start date of a habit event
     * @param date (Date): The new start date
     */
    public void setDate(Date date) {
        _date = date;
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
    // @PropertyName("picture")
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

    /**
     * Gets the ID of the current habit event
     * @return habit event's id
     */
    @PropertyName("id")
    public String getId() { return _id; }

    /**
     * Sets the ID of the habit event
     * This function SHOULD not be used
     * @param id new id for habit event
     */
    public void setId(String id) {_id = id; }
}
