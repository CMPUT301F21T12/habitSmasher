package com.example.habitsmasher.ui;

import static org.junit.Assert.*;

import com.example.habitsmasher.PasswordEncrypt;

import org.junit.Before;
import org.junit.Test;

public class PasswordEncryptTest {

    private PasswordEncrypt _passwordEncrypt;
    @Before
    public void setUp(){
        _passwordEncrypt = new PasswordEncrypt();
    }

    @Test
    public void encrypt_password_expectTrue() {
        String password = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890-=!@#$%^&*()_+[]{}|;:',.<>/?";
        String encryptedPassword = _passwordEncrypt.encrypt(password);
        assertEquals("HIJKLMNOPQRSTUVWXYZ[\\]^_`ahijklmnopqrstuvwxyz{|}~qrs89:;<=>?@74D(G*+,e-1/0f2bdtvuBA.35CE6F", encryptedPassword);
    }
}
