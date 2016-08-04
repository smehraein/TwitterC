package com.codepath.apps.twittercproject;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.codepath.apps.twittercproject.models.Tweet;

import java.util.List;

/**
 * Author: soroushmehraein
 * Project: TwitterCProject
 * Date: 8/3/16
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetsArrayAdapter(Context context, List<Tweet> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }
}
