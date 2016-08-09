package com.codepath.apps.twittercproject.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.twittercproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity {

//    private static int REQUEST_POST_TWEET = 20;

    @BindView(R.id.tbTimeline)
    Toolbar tbTimeline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
        setSupportActionBar(tbTimeline);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    public void onClickCompose(MenuItem item) {
//        Intent intent = new Intent(TimelineActivity.this, ComposeActivity.class);
//        startActivityForResult(intent, REQUEST_POST_TWEET);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_POST_TWEET && resultCode == RESULT_OK) {
//            Tweet tweet = (Tweet) data.getSerializableExtra(Tweet.INTENT_TWEET);
//            tweets.add(0, tweet);
//            aTweets.notifyItemInserted(0);
//            layoutManager.scrollToPosition(0);
//        }
//    }
}
