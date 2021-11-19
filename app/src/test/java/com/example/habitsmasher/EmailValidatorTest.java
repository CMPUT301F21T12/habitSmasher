package com.example.habitsmasher;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class EmailValidatorTest {
    private static final String VALID_EMAIL = "goodEmail@gmail.com";
    private static final String INVALID_EMAIL = "badEmail123";

    private EmailValidator _emailValidator;

    @Before
    public void setUp(){
        _emailValidator =  new EmailValidator();
    }

    @Test
    public void isEmailValid_emptyEmail_expectFalse() {
        assertFalse(_emailValidator.isEmailValid(""));
    }

    @Test
    public void isEmailValid_invalidEmail_expectFalse() {
        assertFalse(_emailValidator.isEmailValid(INVALID_EMAIL));
    }

    @Test
    public void isEmailValid_validEmail_expectTrue() {
        assertTrue(_emailValidator.isEmailValid(VALID_EMAIL));
    }
}
