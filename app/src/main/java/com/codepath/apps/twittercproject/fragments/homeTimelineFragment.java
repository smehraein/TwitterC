package com.codepath.apps.twittercproject.fragments;

import android.util.Log;

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
public class HomeTimelineFragment extends TweetsListFragment {
    @Override
    protected void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                aTweets.clear();
                dbManager.deleteAllTweets(Tweet.TIMELINES_ENUM.HOME_TIMELINE);
                List<Tweet> tweets = Tweet.fromJSONArray(response, Tweet.TIMELINES_ENUM.HOME_TIMELINE);
                tweets = dbManager.mergeTweetsToDatabase(tweets);
                aTweets.addAll(tweets);
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", "Could not fetch from Twitter");
                swipeContainer.setRefreshing(false);
            }
        });
    }

    protected void populateFromDb() {
        List<Tweet> dbTweets = dbManager.getAllTweets(Tweet.TIMELINES_ENUM.HOME_TIMELINE);
        aTweets.addAll(dbTweets);
    }

    @Override
     protected void getMoreTweets(long maxId) {
        client.getHomeTimeline(maxId, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                List<Tweet> tweets = Tweet.fromJSONArray(response, Tweet.TIMELINES_ENUM.HOME_TIMELINE);
                tweets = dbManager.mergeTweetsToDatabase(tweets);
                aTweets.addAll(tweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", "Could not fetch from Twitter");
            }
        });
    }
}
