package com.codepath.apps.twittercproject.fragments;

import android.util.Log;

import com.activeandroid.query.Select;
import com.codepath.apps.twittercproject.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Author: soroushmehraein
 * Project: TwitterCProject
 * Date: 8/9/16
 */
public class home_timeline_fragment extends tweets_list_fragment {
    @Override
    void populateTimeline() {
        populateFromDb();
        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                aTweets.clear();
                dbManager.deleteAllTweets();
                aTweets.addAll(Tweet.fromJSONArray(response));
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private void populateFromDb() {
        List<Tweet> dbTweets = new Select().from(Tweet.class).execute();
        aTweets.addAll(dbTweets);
    }

    @Override
    void getMoreTweets(long maxId) {
        client.getHomeTimeline(maxId, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                aTweets.addAll(Tweet.fromJSONArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
}
