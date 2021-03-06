package com.example.habitsmasher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * This class is used to represent the days of the week a habit takes place, and contains all
 * functionality in storing, querying, modifying, and returning what days of the week a habit
 * takes place. This class does not currently handle storing the days in a database,
 * but is capable of taking in information stored in a database and converting to
 * a DaysTracker object.
 *
 * @author: Cameron Matthew
 * @Version: 1.0
 * @see Habit
 */
public class DaysTracker {
    //instance variables to represent days of the week
    private ArrayList<Boolean> _days;
    private String[] _daysIndexHelper = {"MO", "TU", "WE", "TH", "FR", "SA", "SU"};

    /**
     * Creates a DaysTracker object with all values set to false. This constructor is the default
     * constructor.
     */
    public DaysTracker(){
        //arraylist of booleans that represent the days of the week. autofilled to 7 booleans.
        _days = new ArrayList<Boolean>(Arrays.asList(new Boolean[7]));

        //fill with all false
        setFalse();
    }

    /**
     * Creates a DaysTracker with {@link String} input. The string contains all of the days that are to
     * be set to true, and all other days false.
     *<p>
     * The string takes the format "day1 day2 ... day7", where each day is the first 2 letters of that day, separated by a space. The string can be in any order.
     * EX) "MO FR WE TH"
     *
     * The string is assumed to be formatted correctly, but will not throw any exceptions if not correctly formatted. If any
     * doubts are present when using this constructor, it may be wise to use the base constructor and set individual days yourself.
     *
     * @param days: The string of dates to use, formatted according to above guide.
     */
    public DaysTracker(String days){

        //create the arrayList
        _days = new ArrayList<Boolean>(Arrays.asList(new Boolean[7]));

        //set all to false
        setFalse();

        //set the specific days
        setDaysFromString(days);


    }

    /**
     * Creates a DaysTracker with arrayList input. This version of the constructor is used to convert data
     * from the database to an object.
     * @param days The list of days as {@link String}
     */
    public DaysTracker(ArrayList<String> days){


        //create the arrayList
        _days = new ArrayList<Boolean>(Arrays.asList(new Boolean[7]));

        //set all to false
        setFalse();

        // convert from string to bool
        setDaysFromList(days);
    }

    /**
     * Sets all of the days to false. Easier way to make a new DaysTracker without making a new DaysTracker.
     */
    public void setFalse(){
        Collections.fill(_days, Boolean.FALSE);
    }

    /**
     * Sets all of the days to true. Ease of use method for quickly setting all days to true.
     */
    public void setTrue(){
        Collections.fill(_days, Boolean.TRUE);
    }

    /**
     * Takes all of the days set to true and adds their abbreviations to a {@link String}, then returns the {@link String}.
     * The string takes the format "day1 day2 ... day7", where each day is the first 2 letters of that day, separated by a space. The string can be in any order.
     * EX) "MO FR WE TH"
     *
     * @return days: String containing all of the days set to true.
     */
    public String getDays(){
        //takes all of the days and concats them together
        //format: first two letters of day. ex) MO -> MONDAY, ex) SA -> SATURDAY
        String dayAbb[] = {"MO", "TU", "WE", "TH", "FR", "SA", "SU"};
        String days = "";

        //for each day
        for (int i = 0; i < 7; i++){
            //if the day is set to true, add the abbreviation to the return string
            if (_days.get(i) == true){
                days += dayAbb[i] + " ";
            }

        }

        days = days.trim();     //trim the excess whitespace at the end
        return days;
    }


    /**
     * This method is the public selector for setting days. It can take either a string or an arraylist
     * and convert the values within into the proper format for the tracker.
     *
     * @param days The days to select, either {@link String} or {@link ArrayList}
     */
    public void setDays(Object days){
        if (days instanceof String) {
            setDaysFromString((String) days);
        }
        else if (days instanceof ArrayList){      // can't specify types in list unfortunately
            setDaysFromList((ArrayList<String>) days);
        }
    }

    private void setDaysFromString(String days){
        //take the string and split it up based on what days are present
        //this will go under the assumption that the string will always be formatted correctly.
        days = days.toUpperCase();
        String[] splitDays = days.split(" ");

        //for each item split from the string
        for(int i = 0; i < splitDays.length; i++){
            //get the value, and if any matches with the abbreviations set the corresponding day to true
            String value = splitDays[i];
            switch (value){
                case "MO":
                    _days.set(0, true);
                    break;
                case "TU":
                    _days.set(1, true);
                    break;
                case "WE":
                    _days.set(2, true);
                    break;
                case "TH":
                    _days.set(3, true);
                    break;
                case "FR":
                    _days.set(4, true);
                    break;
                case "SA":
                    _days.set(5, true);
                    break;
                case "SU":
                    _days.set(6, true);
                    break;
            }
        }
    }

    private void setDaysFromList(ArrayList<String> days){
        // processes each line through the string processor
        for (int i = 0; i < days.size(); i++) {
            setDaysFromString(days.get(i));
        }
    }
    /**
     * Returns the list of days.
     * @return Entire list of days
     */
    public ArrayList<Boolean> getList(){return _days;}

    /**
     * Returns the list of days with the days as strings instead of booleans
     * @return List of all days
     */
    public ArrayList<String> getListWithStrings(){
        ArrayList<String> returnList = new ArrayList<>();
        // convert from bool to string
        for (int i = 0; i < 7; i++) {
            if (_days.get(i)){
                returnList.add(_daysIndexHelper[i]);
            }
        }
        return returnList;
    }


    /**
     * Returns the value set to Monday, either true or false.
     * @return {@link Boolean} value of the day.
     */
    public Boolean getMonday() {
        return _days.get(0);
    }

    /**
     * Sets the value of Monday, either true or false.
     * @param val the {@link Boolean} value to set.
     */
    public void setMonday(Boolean val) {
        _days.set(0, val);
    }

    /**
     * Returns the value set to Tuesday, either true or false.
     * @return {@link Boolean} value of the day.
     */
    public Boolean getTuesday() {
        return _days.get(1);
    }

    /**
     * Sets the value of Tuesday, either true or false.
     * @param val the {@link Boolean} value to set.
     */
    public void setTuesday(Boolean val) {
        _days.set(1, val);
    }

    /**
     * Returns the value set to Wednesday, either true or false.
     * @return {@link Boolean} value of the day.
     */
    public Boolean getWednesday() {
        return _days.get(2);
    }

    /**
     * Sets the value of Wednesday, either true or false.
     * @param val the {@link Boolean} value to set.
     */
    public void setWednesday(Boolean val) {
        _days.set(2, val);
    }

    /**
     * Returns the value set to Thursday, either true or false.
     * @return {@link Boolean} value of the day.
     */
    public Boolean getThursday() {
        return _days.get(3);
    }

    /**
     * Sets the value of Thursday, either true or false.
     * @param val the {@link Boolean} value to set.
     */
    public void setThursday(Boolean val) {
        _days.set(3, val);
    }

    /**
     * Returns the value set to Friday, either true or false.
     * @return {@link Boolean} value of the day.
     */
    public Boolean getFriday() {
        return _days.get(4);
    }

    /**
     * Sets the value of Friday, either true or false.
     * @param val the {@link Boolean} value to set.
     */
    public void setFriday(Boolean val) {
        _days.set(4, val);
    }

    /**
     * Returns the value set to Saturday, either true or false.
     * @return {@link Boolean} value of the day.
     */
    public Boolean getSaturday() {
        return _days.get(5);
    }

    /**
     * Sets the value of Saturday, either true or false.
     * @param val the {@link Boolean} value to set.
     */
    public void setSaturday(Boolean val) {
        _days.set(5, val);
    }

    /**
     * Returns the value set to Sunday, either true or false.
     * @return {@link Boolean} value of the day.
     */
    public Boolean getSunday() {
        return _days.get(6);
    }

    /**
     * Sets the value of Sunday, either true or false.
     * @param val the {@link Boolean} value to set.
     */
    public void setSunday(Boolean val) {
        _days.set(6, val);
    }
}
