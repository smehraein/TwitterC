package com.codepath.apps.twittercproject.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

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

    // Public constructor for ActiveAndroid
    public User() {
        super();
    }

    private static User fromJSON(JSONObject jsonObject) {
        User user = new User();
        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static User getUserFromJSON(JSONObject jsonObject) {
        long uid = -1;
        User user;
        try {
            uid = jsonObject.getLong("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        user = new Select().from(User.class).where("uid = ?", uid).executeSingle();
        if (user == null) {
            user = fromJSON(jsonObject);
            user.save();
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
}
