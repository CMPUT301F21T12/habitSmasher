package com.example.habitsmasher;

import android.media.Image;

import java.util.ArrayList;

/**
 * The User class models a user and holds their credentials and corresponding information
 */
public class User {
    private String _username;
    private String _password;
    private final ArrayList<String> _followerList;
    private final ArrayList<String> _followingList;
    private Image _profilePicture;
    private static final HabitList _habits = new HabitList();

    /**
     * This lightweight constructor initializes a new user with empty lists
     * @param username the username
     * @param password the password
     */
    public User(String username,
                 String password) {
        _username = username;
        _password = password;
        _followerList = new ArrayList<>();
        _followingList = new ArrayList<>();
    }

    /**
     * This heavy-duty constructor provides the ability to create a new user with control over all
     * parameters/fields
     * @param username the username
     * @param password the password
     * @param followerList the list of followers
     * @param followingList the list of users following
     * @param profilePicture the profile picture of the user
     */
    public User(String username,
                 String password,
                 ArrayList<String> followerList,
                 ArrayList<String> followingList,
                 Image profilePicture) {
        _username = username;
        _password = password;
        _followerList = followerList;
        _followingList = followingList;
        _profilePicture = profilePicture;
    }

    public String getUsername() {
        return _username;
    }

    public void setUsername(String username) {
        _username = username;
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        _password = password;
    }

    public ArrayList<String> getFollowers() {
        return _followerList;
    }

    /**
     * This method adds a new follower for the user
     * @param followerUsername the new follower user
     */
    public void addNewFollower(String followerUsername) {
        _followerList.add(followerUsername);
    }

    public ArrayList<String> getFollowingList() {
        return _followingList;
    }

    /**
     * This method adds a new user to the following list
     * @param username the user to be followed
     */
    public void followNewUser(String username) {
        _followingList.add(username);
    }

    public Image getProfilePicture() {
        return _profilePicture;
    }

    public void setProfilePicture(Image picture) {
        _profilePicture = picture;
    }

    /**
     * This method returns the number of followers this user has
     * @return number of followers
     */
    public int getFollowerCount() {
        return _followerList.size();
    }

    /**
     * This method returns the number of users this user is following
     * @return the number of users following
     */
    public int getFollowingCount() {
        return _followingList.size();
    }

    /**
     * This method gets the list of habits associated with this user
     * @return
     */
    public HabitList getHabits() {
        return _habits;
    }
}
