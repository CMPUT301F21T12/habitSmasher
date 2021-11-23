package com.example.habitsmasher;

/**
 * This class encrypts the password the user enters to be stored in the database.
 * @author Jacob Nguyen
 */
public class PasswordEncrypt {

    public static String encrypt(String password) {
        int key = 7;
        String newPassword = "";
        for (int i = 0; i < password.length(); i++) {
            if (password.charAt(i) > Math.abs(127-key)) {
                newPassword += (char) (password.charAt(i) - key);
            } else {
                newPassword += (char) (password.charAt(i) + key);
            }
        }
        return newPassword;
    }
}