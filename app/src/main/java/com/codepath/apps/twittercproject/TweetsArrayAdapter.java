package com.codepath.apps.twittercproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twittercproject.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: soroushmehraein
 * Project: TwitterCProject
 * Date: 8/3/16
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetsArrayAdapter(Context context, List<Tweet> objects) {
        super(context, R.layout.item_tweet, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvUserName.setText(tweet.getUser().getScreenName());
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.tvDate.setText(tweet.getCreatedAtRelative());
        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(viewHolder.ivProfileImage);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tvUserName) TextView tvUserName;
        @BindView(R.id.tvBody) TextView tvBody;
        @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
        @BindView(R.id.tvDate) TextView tvDate;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
