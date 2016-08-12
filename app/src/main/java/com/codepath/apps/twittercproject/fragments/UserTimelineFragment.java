package com.codepath.apps.twittercproject.fragments;

import android.os.Bundle;
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
 * Date: 8/11/16
 */
public class UserTimelineFragment extends TweetsListFragment {
    @Override
    protected void populateTimeline() {
        String screenName = getArguments().getString("screen_name");
        client.getUserTimeline(screenName, new JsonHttpResponseHandler(){
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

    @Override
    protected void getMoreTweets(long maxId) {

    }

    @Override
    protected void populateFromDb() {

    }

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }
}
