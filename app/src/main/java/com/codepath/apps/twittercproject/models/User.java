package com.codepath.apps.twittercproject.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Author: soroushmehraein
 * Project: TwitterCProject
 * Date: 8/3/16
 */
@Table(name = "User")
public class User extends Model implements Serializable {
    @Column(name = "Name")
    private String name;
    @Column(name = "uid", unique = true)
    private long uid;
    @Column(name = "Screen_Name")
    private String screenName;
    @Column(name = "Profile_Image")
    private String profileImageUrl;
    @Column(name = "Tagline")
    private String tagline;
    @Column(name = "Followers")
    private int followersCount;
    @Column(name = "Following")
    private int followingCount;

    // Public constructor for ActiveAndroid
    public User() {
        super();
    }

    public static User fromJSON(JSONObject jsonObject) {
        User user = new User();
        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.tagline= jsonObject.getString("description");
            user.followingCount = jsonObject.getInt("friends_count");
            user.followersCount = jsonObject.getInt("followers_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getTagline() {
        return tagline;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }
}
