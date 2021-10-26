package com.example.habitsmasher.test;

import static org.junit.Assert.*;

import com.example.habitsmasher.User;

import org.junit.Before;
import org.junit.Test;

public class UserUnitTest {
    private User _user;

    @Before
    public void setUp() {
        _user = new User("testUser", "123");
    }

    @Test
    public void addNewFollower_followNewUser_expectCountUpdated() {
        assertEquals(0, _user.getFollowerCount());

        _user.addNewFollower("newUser1");
        _user.addNewFollower("newUser2");
        _user.addNewFollower("newUser3");

        assertEquals(3, _user.getFollowerCount());
    }

    @Test
    public void followNewUser_followingNewUser_expectCountUpdated() {
        assertEquals(0, _user.getFollowingCount());

        _user.followNewUser("newUser1");
        _user.followNewUser("newUser2");
        _user.followNewUser("newUser3");

        assertEquals(3, _user.getFollowingCount());
    }
}