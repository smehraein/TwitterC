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
public class MentionsTimelineFragment extends TweetsListFragment {

    @Override
    protected void populateTimeline() {
        client.getMentionsTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                aTweets.clear();
                aTweets.addAll(Tweet.fromJSONArray(response, Tweet.TIMELINES_ENUM.MENTIONS_TIMELINE));
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                swipeContainer.setRefreshing(false);
            }
        });
    }

    protected void populateFromDb() {
        List<Tweet> dbTweets = dbManager.getAllTweets(Tweet.TIMELINES_ENUM.MENTIONS_TIMELINE);
        aTweets.addAll(dbTweets);
    }

    @Override
    protected void getMoreTweets(long maxId) {
        client.getMentionsTimeline(maxId, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                aTweets.addAll(Tweet.fromJSONArray(response, Tweet.TIMELINES_ENUM.MENTIONS_TIMELINE));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
}
