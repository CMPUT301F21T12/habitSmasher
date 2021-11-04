package com.example.habitsmasher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * This class is used to represent the days of the week a habit takes place, and contains all functionality in
 * storing, querying, modifying, and returning what days of the week a habit takes place. This class does not currently handle
 * storing the days in a database, but is capable of taking in information stored in a database and converting to a DaysTracker object.
 *
 * @author: Cameron Matthew
 * @Version: 1.0
 * @see Habit
 */

public class DaysTracker {


    //instance variables to represent days of the week
    private ArrayList<Boolean> _days;

    /**
     * Creates a DaysTracker object with all values set to false. This constructor is the default constructor.
     */
    public DaysTracker(){
        //arraylist of booleans that represent the days of the week. autofilled to 7 bools.
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
        setDays(days);


    }

    /* Was experimenting with hashmaps and iterators, if anyone thinks this may be useful I will complete it,
     * If not I'll get rid of it.

    public DaysTracker(HashMap<String, Boolean> map){
        _days = new ArrayList<Boolean>(Arrays.asList(new Boolean[7]));
        Collections.fill(_days, Boolean.FALSE);
        Set<String> allKeys = map.keySet();

        //iterator over key set
        Iterator<String> keyIterator = allKeys.iterator();
        while(keyIterator.hasNext()){
            switch(keyIterator.next()){

            }
        }
    }
     */

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
     * Takes a string with the days and converts them to days of the week.
     * The string takes the format "day1 day2 ... day7", where each day is the first 2 letters of that day, separated by a space. The string can be in any order.
     * EX) "MO FR WE TH"
     *
     * @param days The days to select
     */
    public void setDays(String days){
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
