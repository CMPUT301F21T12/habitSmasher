package com.example.habitsmasher;
import android.location.Location;
import android.net.Uri;
import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.Date;

/**
 * This is the Habit Event class
 * Its purpose is to store and retrieve the start date, comments, picture, and location of a habit event
 * Pictures for habit events are not yet implemented
 */
public class HabitEvent extends DatabaseEntity implements Serializable {
    private Date _date;
    private String _comment;
    private Uri _pictureUri;
    private Location _location;

    // TODO: Fully integrate pictures

    /**
     * Empty constructor
     */
    public HabitEvent() {
        // Needed for firestore
    }
    public HabitEvent(Date startDate, String comment, String id) {
        super(id);
        _date = startDate;
        _comment = comment;
        _location = null;
    }

    /**
     * Constructor, creates new HabitEvent with location
     * @param startDate (Date): The start date of the habit event
     * @param comment (String): The comment of the habit event
     */
    public HabitEvent(Date startDate, String comment, String id, Location location) {
        super(id);
        _date = startDate;
        _comment = comment;
        _location = location;
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

    @PropertyName("location")
    public Location getLocation() {
        return _location;
    }

    public void setLocation(Location location) {
        _location = location;
    }


}
