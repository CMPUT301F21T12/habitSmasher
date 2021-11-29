package com.example.habitsmasher.test;

import static org.junit.Assert.*;

import com.example.habitsmasher.User;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class UserTest {
    private ArrayList<String> EMPTY_FOLLOWER_LIST = new ArrayList<>();
    private ArrayList<String> EMPTY_FOLLOWING_LIST = new ArrayList<>();
    private ArrayList<String> EMPTY_REQUEST_LIST = new ArrayList<>();
    private User _user;

    @Before
    public void setUp() {
        _user = new User("1",
                    "testUser",
                       "123@gmail.com",
                     "123",
                              EMPTY_FOLLOWER_LIST,
                              EMPTY_FOLLOWING_LIST,
                              EMPTY_REQUEST_LIST);
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

    @Test
    public void testAddFollowRequest_expectFollowRequestAdded() {
        assertTrue(_user.getFollowRequests().isEmpty());
        _user.addFollowRequest("newUser1");
        assertEquals(1, _user.getFollowRequests().size());
        assertTrue(_user.getFollowRequests().contains("newUser1"));
    }

    @Test
    public void testAddExistingFollowRequest_expectFollowRequestNotAdded() {
        assertTrue(_user.getFollowRequests().isEmpty());
        _user.addFollowRequest("newUser1");
        assertEquals(1, _user.getFollowRequests().size());
        assertTrue(_user.getFollowRequests().contains("newUser1"));
        _user.addFollowRequest("newUser1");
        assertEquals(1, _user.getFollowRequests().size());
    }

    @Test
    public void testRemoveExistingFollowRequest_expectFollowRequestRemoved() {
        assertTrue(_user.getFollowRequests().isEmpty());
        _user.addFollowRequest("newUser1");
        assertEquals(1, _user.getFollowRequests().size());
        _user.deleteFollowRequest("newUser1");
        assertEquals(0, _user.getFollowRequests().size());
        assertFalse(_user.getFollowRequests().contains("newUser1"));
    }


}