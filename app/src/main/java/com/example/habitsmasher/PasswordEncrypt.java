package com.example.habitsmasher;

public class PasswordEncrypt {

    private static final String BEGINNING_RANDOM_CHARACTERS = "scjOsmSpTHF#BB98w9%*bDSVcdbHUgafFVvbfd*b89FB398^BW9b(b9B";
    private String ENDING_RANDOM_CHARACTERS = "lB28PG&$d8RHSA98tr3aJENB9T3mntcwVfM6NMrMUcidgD&s9y3rDw*&^3rdS";
    int _key = 7;

    public String encrypt(String password) {
        //_key = new Random().nextInt(10);
        String newPassword = "";

        newPassword += BEGINNING_RANDOM_CHARACTERS;
        for (int i = 0; i<password.length(); i++) {
            if (password.charAt(i) > 127-_key) {
                newPassword += (char)(password.charAt(i) - _key);
            } else {
                newPassword += (char)(password.charAt(i) + _key);
            }

        }
        newPassword += ENDING_RANDOM_CHARACTERS;
        newPassword += _key;
        return newPassword;
    }
}
