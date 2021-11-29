package com.example.habitsmasher;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import android.content.Context;

import androidx.fragment.app.Fragment;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class UserAccountHelperTest {

    private ArrayList<String> EMPTY_FOLLOWER_LIST = new ArrayList<>();
    private ArrayList<String> EMPTY_FOLLOWING_LIST = new ArrayList<>();
    private ArrayList<String> EMPTY_REQUEST_LIST = new ArrayList<>();

    @Test
    public void buildUserDataMap_expectCorrectMapConstructed() {
        UserAccountHelper userAccountHelper = new UserAccountHelper(mock(Context.class),
                                                                    mock(Fragment.class));

        User testUser = new User("test",
                                 "testUser",
                                 "test@gmail.com",
                                 "123456",
                                 EMPTY_FOLLOWER_LIST,
                                 EMPTY_FOLLOWING_LIST,
                                 EMPTY_REQUEST_LIST);

        HashMap<String, Object> actualUserDataMap = userAccountHelper.buildUserDataMap(testUser);

        assertEquals(testUser.getUsername(), actualUserDataMap.get("username"));
        assertEquals(testUser.getEmail(), actualUserDataMap.get("email"));
        assertEquals(testUser.getId(), actualUserDataMap.get("id"));
    }
}