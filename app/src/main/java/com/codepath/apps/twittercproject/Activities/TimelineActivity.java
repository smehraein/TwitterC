package com.codepath.apps.twittercproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.activeandroid.query.Select;
import com.codepath.apps.twittercproject.Adapters.TweetsAdapter;
import com.codepath.apps.twittercproject.Adapters.EndlessRecyclerViewScrollListener;
import com.codepath.apps.twittercproject.R;
import com.codepath.apps.twittercproject.TwitterApplication;
import com.codepath.apps.twittercproject.TwitterClient;
import com.codepath.apps.twittercproject.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.swipeContainerTimeline)
    SwipeRefreshLayout swipeContainer;

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

        setupView();
        populateFromDb();

        // Get client and populate
        client = TwitterApplication.getRestClient();
        populateTimeline();
    }

    private void populateFromDb() {
        List<Tweet> dbTweets = new Select().from(Tweet.class).execute();
        aTweets.addAll(dbTweets);
    }

    private void setupView() {
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

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline();
            }
        });
    }

    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                aTweets.clear();
                aTweets.addAll(Tweet.fromJSONArray(response));
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private void getMoreTweets(long maxId) {
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
