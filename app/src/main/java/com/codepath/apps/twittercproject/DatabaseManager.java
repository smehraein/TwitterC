package com.codepath.apps.twittercproject;

import com.activeandroid.query.Delete;
import com.codepath.apps.twittercproject.models.User;

/**
 * Author: soroushmehraein
 * Project: TwitterCProject
 * Date: 8/9/16
 */
public class DatabaseManager {

    private static DatabaseManager ourInstance = new DatabaseManager();

    public static DatabaseManager getInstance() {
        return ourInstance;
    }

    private DatabaseManager() {
    }

    public void deleteAllTweets() {
        new Delete().from(User.class).execute();
    }
}
