package com.codepath.apps.twittercproject;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.codepath.apps.twittercproject.models.Tweet;
import com.codepath.apps.twittercproject.models.User;

import java.util.ArrayList;
import java.util.List;

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

    public List<Tweet> getAllTweets() {
        return new Select().from(Tweet.class).execute();
    }

    public List<Tweet> getAllTweets(Tweet.TIMELINES_ENUM timeline) {
        return new Select().from(Tweet.class).where("Timeline = ?", timeline.ordinal()).orderBy("uid DESC").execute();
    }

    public void deleteAllTweets() {
        new Delete().from(User.class).execute();
    }

    public void deleteAllTweets(Tweet.TIMELINES_ENUM timeline) {
        new Delete().from(Tweet.class).where("Timeline = ?", timeline.ordinal()).execute();
    }

    public void deleteUsersWithoutTweets() {
        List<User> users = new Select("User").distinct().from(Tweet.class).execute();
        ArrayList<Long> userIds = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            userIds.add(users.get(i).getUid());
        }
        new Delete().from(User.class).where("uid NOT IN ?", userIds).execute();
    }

    public ArrayList<Tweet> mergeTweetsToDatabase(List<Tweet> tweets) {
        ArrayList<Tweet> dbTweets = new ArrayList<>();
        for (int i = 0; i < tweets.size(); i++) {
            Tweet tweet = tweets.get(i);
            Tweet dbTweet = new Select().from(Tweet.class).where("uid = ?", tweet.getUid()).and("Timeline = "
                    + "?", tweet.getTimeline()).executeSingle();
            if (dbTweet == null) {
                User dbUser = new Select().from(User.class).where("uid = ?", tweet.getUser().getUid()).executeSingle();
                if (dbUser == null) {
                    tweet.getUser().save();
                } else {
                    tweet.setUser(dbUser);
                }
                tweet.save();
                dbTweets.add(tweet);
            } else {
                dbTweets.add(dbTweet);
            }
        }
        return dbTweets;
    }

    public void saveTweet(Tweet tweet) {
        User dbUser = new Select().from(User.class).where("uid = ?", tweet.getUser().getUid()).executeSingle();
        if (dbUser == null) {
            tweet.getUser().save();
        }
        tweet.save();
    }
}
