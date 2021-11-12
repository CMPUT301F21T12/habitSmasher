package com.example.habitsmasher;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UserValidatorTest {
    private static final String VALID_USERNAME = "validUsername";
    private static final String VALID_EMAIL = "goodEmail@gmail.com";
    private static final String VALID_PASSWORD = "validPassword";
    private static final String INVALID_USERNAME = "ThisIsAReallyReallyBadUsername";
    private static final String INVALID_EMAIL = "badEmail123";
    private static final String INVALID_PASSWORD = "ThisPasswordIsWayyyTooLongForThisApp";

    private UserValidator _validator;


    @Before
    public void setUp() {
        _validator = new UserValidator();
    }

    @Test
    public void getValidUsername_validUser_returnsUsername(){
        _validator._username = VALID_USERNAME;
        _validator._email = VALID_EMAIL;
        _validator._password = VALID_PASSWORD;

        assertEquals(VALID_USERNAME, _validator.getValidUsernameForSignUp());
    }

    @Test
    public void getValidEmail_validUser_returnsEmail(){
        _validator._username = VALID_USERNAME;
        _validator._email = VALID_EMAIL;
        _validator._password = VALID_PASSWORD;

        assertEquals(VALID_EMAIL, _validator.getValidEmailForSignUp());
    }

    @Test
    public void getValidPassword_validUser_returnsPassword(){
        _validator._username = VALID_USERNAME;
        _validator._email = VALID_EMAIL;
        _validator._password = VALID_PASSWORD;

        assertEquals(VALID_PASSWORD, _validator.getValidPasswordForSignUp());
    }

    @Test
    public void getValidUsernameForSignup_invalidUser_returnsEmpty(){
        _validator._username = INVALID_USERNAME;
        _validator._email = VALID_EMAIL;
        _validator._password = VALID_PASSWORD;

        assertEquals("", _validator.getValidUsernameForSignUp());
    }

    @Test
    public void getValidEmailForSignup_invalidUser_returnsEmpty(){
        _validator._username = INVALID_USERNAME;
        _validator._email = VALID_EMAIL;
        _validator._password = VALID_PASSWORD;

        assertEquals("", _validator.getValidEmailForSignUp());
    }

    @Test
    public void getValidPasswordForSignup_invalidUser_returnsEmpty(){
        _validator._username = INVALID_USERNAME;
        _validator._email = VALID_EMAIL;
        _validator._password = VALID_PASSWORD;

        assertEquals("", _validator.getValidPasswordForSignUp());
    }

    @Test
    public void getValidEmailForLogin_invalidUser_returnsEmpty(){
        _validator._email = INVALID_EMAIL;
        _validator._password = VALID_PASSWORD;

        assertEquals("", _validator.getValidEmailForLogin());
    }

    @Test
    public void getValidPasswordForLogin_invalidUser_returnsEmpty(){
        _validator._email = VALID_EMAIL;
        _validator._password = INVALID_PASSWORD;

        assertEquals("", _validator.getValidPasswordForLogin());
    }

    @Test
    public void isNewUserValid_validUser_expectTrue() {
        assertTrue(_validator.isNewUserValid(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD));
    }

    @Test
    public void isLoginValid_validUser_expectTrue() {
        assertTrue(_validator.isLoginValid(VALID_EMAIL, VALID_PASSWORD));
    }

    @Test
    public void isNewUserValid_invalidUsername_expectFalse() {
        assertFalse(_validator.isNewUserValid(INVALID_USERNAME, VALID_EMAIL, VALID_PASSWORD));
    }

    @Test
    public void isNewUserValid_emptyUsername_expectFalse() {
        assertFalse(_validator.isNewUserValid("", VALID_EMAIL, VALID_PASSWORD));
    }

    @Test
    public void isLoginValid_invalidEmail_expectFalse() {
        assertFalse(_validator.isLoginValid(INVALID_EMAIL, VALID_PASSWORD));
    }

    @Test
    public void isLoginValid_invalidPassword_expectFalse() {
        assertFalse(_validator.isLoginValid(VALID_EMAIL, INVALID_PASSWORD));
    }

    @Test
    public void isLoginValid_emptyEmail_expectFalse() {
        assertFalse(_validator.isLoginValid("", VALID_PASSWORD));
    }

    @Test
    public void isLoginValid_emptyPassword_expectFalse() {
        assertFalse(_validator.isLoginValid(VALID_EMAIL, ""));
    }

}