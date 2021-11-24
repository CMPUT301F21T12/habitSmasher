package com.example.habitsmasher;

/**
 * This class encrypts the password the user enters to be stored in the database.
 * @author Jacob Nguyen
 */
public class PasswordEncrypt {

    private static final int ASCII_TABLE_MAX = 126;

    /**
     * Caesar Cipher the password to store in the database.
     * @param password the user enters
     * @return return the password in encrypted form
     */
    public static String encrypt(String password) {

        int key = 7;
        String newPassword = "";
        for (int i = 0; i < password.length(); i++) {
            if (password.charAt(i) > ASCII_TABLE_MAX-key) {
                newPassword += (char) (password.charAt(i) - key);
            } else {
                newPassword += (char) (password.charAt(i) + key);
            }
        }
        return newPassword;
    }
}