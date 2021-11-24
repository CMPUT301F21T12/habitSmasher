package com.example.habitsmasher.ui;

import java.util.ArrayList;

public class FollowList extends ArrayList<String> {
    // arraylist of users
    private ArrayList<String> _users = new ArrayList<>();

    public ArrayList<String> getFollowList() {
        return _users;
    }
}
