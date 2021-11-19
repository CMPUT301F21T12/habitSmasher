package com.example.habitsmasher.test;

import static org.junit.Assert.*;

import com.example.habitsmasher.User;

import org.junit.Before;
import org.junit.Test;

public class UserTest {
    private User _user;

    @Before
    public void setUp() {
        _user = new User("1", "testUser", "123@gmail.com", "123");
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

        _user.followUser("newUser1");
        _user.followUser("newUser2");
        _user.followUser("newUser3");

        assertEquals(3, _user.getFollowingCount());
    }
}