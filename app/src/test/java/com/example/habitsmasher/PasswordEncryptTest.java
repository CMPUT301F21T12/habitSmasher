package com.example.habitsmasher;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class PasswordEncryptTest {

    private static final String ORIGINAL_PASSWORD =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890-=!@#$%^&*()_+[]{}|;:',.<>/?";
    private static final String ENCRYPTED_PASSWORD =
            "HIJKLMNOPQRSTUVWXYZ[\\]^_`ahijklmnopqrstuvwxyz{|}~qrs89:;<=>?@74D(G*+,e-1/0f2bdtvuBA.35CE6F";

    private PasswordEncrypt _passwordEncrypt;
    @Before
    public void setUp(){
        _passwordEncrypt = new PasswordEncrypt();
    }

    @Test
    public void encrypt_password_expectEqual() {
        String encryptedPassword = _passwordEncrypt.encrypt(ORIGINAL_PASSWORD);
        assertEquals(ENCRYPTED_PASSWORD, encryptedPassword);
    }
}
