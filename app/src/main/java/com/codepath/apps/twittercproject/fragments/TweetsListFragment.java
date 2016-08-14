package com.codepath.apps.twittercproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.twittercproject.Adapters.EndlessRecyclerViewScrollListener;
import com.codepath.apps.twittercproject.Adapters.ItemClickSupport;
import com.codepath.apps.twittercproject.Adapters.TweetsAdapter;
import com.codepath.apps.twittercproject.DatabaseManager;
import com.codepath.apps.twittercproject.R;
import com.codepath.apps.twittercproject.TwitterApplication;
import com.codepath.apps.twittercproject.TwitterClient;
import com.codepath.apps.twittercproject.models.Tweet;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: soroushmehraein
 * Project: TwitterCProject
 * Date: 8/8/16
 */
public abstract class TweetsListFragment extends Fragment {

    private Unbinder unbinder;
    protected TwitterClient client;
    protected DatabaseManager dbManager;
    private ArrayList<Tweet> tweets;
    protected TweetsAdapter aTweets;
    private LinearLayoutManager layoutManager;
    @BindView(R.id.rvTweets)
    RecyclerView rvTweets;
    @BindView(R.id.swipeContainerTimeline)
    SwipeRefreshLayout swipeContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        aTweets = new TweetsAdapter(getActivity(), tweets);
        setupView();
        populateFromDb();
        populateTimeline();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
        client = TwitterApplication.getRestClient();
        dbManager = TwitterApplication.getDatabaseManager();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setupView() {
        // Bind array adapter to recycler view
        rvTweets.setAdapter(aTweets);
        layoutManager = new LinearLayoutManager(getActivity());
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

        ItemClickSupport.addTo(rvTweets).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Tweet tweet = tweets.get(position);
                FragmentManager manager = getFragmentManager();
                TweetDetailFragment tweetDetailFragment = TweetDetailFragment.newInstance(tweet);
                tweetDetailFragment.show(manager, "fragment_tweet_detail");
            }
        });
    }

    public void addTweet(Tweet tweet) {
        tweets.add(0, tweet);
        aTweets.notifyItemInserted(0);
        layoutManager.scrollToPosition(0);
    }

    protected abstract void populateTimeline();

    protected abstract void getMoreTweets(long maxId);

    protected abstract void populateFromDb();
}
