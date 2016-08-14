package com.codepath.apps.twittercproject.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twittercproject.R;
import com.codepath.apps.twittercproject.models.Tweet;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: soroushmehraein
 * Project: TwitterCProject
 * Date: 8/14/16
 */
public class TweetDetailFragment extends DialogFragment {

    @BindView(R.id.ivDetailProfilePic)
    ImageView ivDetailProfilePic;
    @BindView(R.id.tvDetailUserName)
    TextView tvDetailUserName;
    @BindView(R.id.tvDetailScreenName)
    TextView tvDetailScreenName;
    @BindView(R.id.tvDetailBody)
    TextView tvDetailBody;

    public TweetDetailFragment() { }

    public static TweetDetailFragment newInstance(Tweet tweet) {
        TweetDetailFragment fragment = new TweetDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("tweet", tweet);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tweet_detail, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        assert savedInstanceState != null;
        Tweet tweet = (Tweet) getArguments().getSerializable("tweet");
        assert tweet != null;
        tvDetailBody.setText(tweet.getBody());
        tvDetailUserName.setText(tweet.getUser().getName());
        tvDetailScreenName.setText(tweet.getUser().getScreenName());
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivDetailProfilePic);
        ivDetailProfilePic.setTag(R.id.screen_name, tweet.getUser().getScreenName());
    }
}
