package com.example.habitsmasher;

import android.media.Image;

import com.google.firebase.firestore.PropertyName;

import java.util.ArrayList;

/**
 * Class that models a user of the app and holds their credentials and corresponding
 * information, such as their habits, followers and users who they are following
 *
 * @author Rudy Patel, Jason Kim, Kaden Dreger
 */
public class User {
    private String _username;
    private String _password;
    private String _email;
    private String _id;
    private ArrayList<String> _followers = new ArrayList<String>();
    private ArrayList<String> _following = new ArrayList<String>();

    // users who have sent follow requests to the user
    private ArrayList<String> _followRequests = new ArrayList<String>();

    private Image _profilePicture;
    private static final HabitList _habits = new HabitList();

    public User() {
        // needed for firebase
    }

    /**
     * This lightweight constructor initializes a new user with empty lists
     * @param email the email
     * @param password the password
     * @param id the id
     * @param username the username
     * @param followers the followers
     * @param following the following
     */
    public User(String id,
                String username,
                String email,
                String password,
                ArrayList<String> followers,
                ArrayList<String> following,
                ArrayList<String> followRequests) {
        _email = email;
        _password = password;
        _username = username;
        _id = id;
        _followers = followers;
        _following = following;
        _followRequests = followRequests;
    }

    /**
     * Gets the username of the user
     * @return username of the user
     */
    @PropertyName("username")
    public String getUsername() {
        return _username;
    }

    /**
     * Sets the username of the user
     * @param username new username of the user
     */
    public void setUsername(String username) {
        _username = username;
    }

    /**
     * Gets the id of the user
     * @return id of the user
     */
    @PropertyName("id")
    public String getId() {
        return _id;
    }

    /**
     * Sets the id of the user
     * @param id new username of the user
     */
    public void setId(String id) {
        _id = id;
    }

    /**
     * Gets the email of the user
     * @return email of the user
     */
    @PropertyName("email")
    public String getEmail() {
        return _email;
    }

    /**
     * Sets the email of the user
     * @param email new username of the user
     */
    public void setEmail(String email) {
        _email = email;
    }

    /**
     * Gets the password of the user
     * @return password of the user
     */
    @PropertyName("password")
    public String getPassword() {
        return _password;
    }

    /**
     * Sets the password of the user
     * @param password new password of user
     */
    public void setPassword(String password) {
        _password = password;
    }

    /**
     * Gets the list of users in the form of usernames
     * that this user is followed by
     * @return list of usernames following this user
     */
    @PropertyName("followers")
    public ArrayList<String> getFollowers() {
        return _followers;
    }

    /**
     * This method adds a new follower for the user
     * @param followerUsername the new follower user
     */
    public void addNewFollower(String followerUsername) {
        _followers.add(followerUsername);
    }

    /**
     * This allows a user to unfollow another user
     * @param followingId
     */
    public void unFollowUser(String followingId) {
        _following.remove(followingId);
    }

    /**
     * Gets the list of users in the form of usernames that
     * this user is following
     * @return list of usernames this user is following
     */
    @PropertyName("following")
    public ArrayList<String> getUsersFollowing() {
        return _following;
    }

    /**
     * This method adds a new user to the following list
     * @param username the user to be followed
     */
    public void followUser(String username) {
        _following.add(username);
    }

    /**
     * Gets the profile picture of the user
     * @return profile picture of user
     */
    public Image getProfilePicture() {
        return _profilePicture;
    }

    /**
     * Sets the profile picture of the user
     * @param picture new profile picture of user
     */
    public void setProfilePicture(Image picture) {
        _profilePicture = picture;
    }

    /**
     * This method returns the number of followers this user has
     * @return number of followers
     */
    public int getFollowerCount() {
        return _followers.size();
    }

    /**
     * This method returns the number of users this user is following
     * @return the number of users following
     */
    public int getFollowingCount() {
        return _following.size();
    }

    /**
     * This method gets the list of habits associated with this user
     * @return HabitList of the user
     */
    public HabitList getHabits() {
        return _habits;
    }

    @PropertyName("followRequests")
    public ArrayList<String> getFollowRequests() {
        return _followRequests;
    }

    public void addFollowRequest(String followingUser) {
        if (_followRequests.contains(followingUser)) {
            return;
        }
        _followRequests.add(followingUser);
    }

    public void deleteFollowRequest(String followingUser) {
        _followRequests.remove(followingUser);
    }

}
