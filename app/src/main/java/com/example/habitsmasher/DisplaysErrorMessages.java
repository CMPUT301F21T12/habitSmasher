package com.example.habitsmasher;

/**
 * Interface implemented by any UI class that displays error messages
 * @author Jason Kim
 */
public interface DisplaysErrorMessages {

    /**
     * Displays the requested error message
     * @param messageType code indicating the type of error message to
     *                    be displayed
     */
    void displayErrorMessage(int messageType);
}

