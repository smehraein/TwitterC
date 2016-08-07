package com.codepath.apps.twittercproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.twittercproject.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private static int REQUEST_POST_TWEET = 20;

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsAdapter aTweets;
    private LinearLayoutManager layoutManager;
    @BindView(R.id.rvTweets)
    RecyclerView rvTweets;
    @BindView(R.id.tbTimeline)
    Toolbar tbTimeline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
        setSupportActionBar(tbTimeline);
        // Create data source
        tweets = new ArrayList<>();
        // Create array adapter
        aTweets = new TweetsAdapter(this, tweets);

        // Bind array adapter to recycler view
        rvTweets.setAdapter(aTweets);
        layoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(layoutManager);
        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Tweet lastTweet = tweets.get(tweets.size() - 1);
                long maxId = lastTweet.getUid() - 1;
                getMoreTweets(maxId);
            }
        });

        // Get client and populate
        client = TwitterApplication.getRestClient();
        populateTimeline();
    }

    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                tweets.clear();
                tweets.addAll(Tweet.fromJSONArray(response));
                aTweets.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    private void getMoreTweets(long maxId) {
        client.getHomeTimeline(maxId, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                tweets.addAll(Tweet.fromJSONArray(response));
                aTweets.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    public void onClickCompose(MenuItem item) {
        Intent intent = new Intent(TimelineActivity.this, ComposeActivity.class);
        startActivityForResult(intent, REQUEST_POST_TWEET);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_POST_TWEET && resultCode == RESULT_OK) {
            Tweet tweet = (Tweet) data.getSerializableExtra(Tweet.INTENT_TWEET);
            tweets.add(0, tweet);
            aTweets.notifyItemInserted(0);
            layoutManager.scrollToPosition(0);
        }
    }
}
