package com.codepath.apps.twittercproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twittercproject.R;
import com.codepath.apps.twittercproject.fragments.HomeTimelineFragment;
import com.codepath.apps.twittercproject.fragments.MentionsTimelineFragment;
import com.codepath.apps.twittercproject.fragments.TweetsListFragment;
import com.codepath.apps.twittercproject.models.Tweet;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity {

    private static int REQUEST_POST_TWEET = 20;

    @BindView(R.id.tbTimeline)
    Toolbar tbTimeline;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabStrip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
        setSupportActionBar(tbTimeline);
        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        tabStrip.setViewPager(viewPager);
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

    public void onProfileView(MenuItem item) {
        Intent intent = new Intent(TimelineActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_POST_TWEET && resultCode == RESULT_OK && viewPager.getCurrentItem() == 0) {
            Tweet tweet = (Tweet) data.getSerializableExtra(Tweet.INTENT_TWEET);
            FragmentManager fragmentManager = getSupportFragmentManager();
            TweetsListFragment fragment = (TweetsListFragment) fragmentManager.getFragments().get(0);
            fragment.addTweet(tweet);

        }
    }

    public void showUserProfile(View view) {
        String screenName = (String) view.getTag(R.id.screen_name);
        Intent intent = new Intent(TimelineActivity.this, ProfileActivity.class);
        intent.putExtra("screen_name", screenName);
        startActivity(intent);
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = { "Home", "Mentions" };

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionsTimelineFragment();
            } else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}
