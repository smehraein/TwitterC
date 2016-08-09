package com.codepath.apps.twittercproject.models;

import android.text.format.DateUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Author: soroushmehraein
 * Project: TwitterCProject
 * Date: 8/3/16
 */
@Table(name = "Tweet")
public class Tweet extends Model implements Serializable {
    public static String INTENT_TWEET = "intent_tweet";

    public enum TIMELINES_ENUM {
        HOME_TIMELINE, MENTIONS_TIMELINE
    }

    @Column(name = "Body")
    private String body;
    @Column(name = "uid")
    private long uid;
    @Column(name = "Created_At")
    private String createdAt;
    @Column(name = "User", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE,
            notNull = true)
    private User user;
    @Column(name = "Timeline")
    private int timeline;

    // Public constructor for ActiveAndroid
    public Tweet() {
        super();
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public int getTimeline() {
        return timeline;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getCreatedAtRelative() {
        return getRelativeTimeAgo(createdAt);
    }

    private String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static Tweet fromJSON(JSONObject jsonObject, TIMELINES_ENUM timeline) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.timeline = timeline.ordinal();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray, TIMELINES_ENUM timeline) {
        ArrayList<Tweet> tweets = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                tweets.add(fromJSON(jsonArray.getJSONObject(i), timeline));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return tweets;
    }
}
