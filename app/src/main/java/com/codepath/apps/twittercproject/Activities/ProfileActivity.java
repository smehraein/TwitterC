package com.codepath.apps.twittercproject.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twittercproject.R;
import com.codepath.apps.twittercproject.TwitterApplication;
import com.codepath.apps.twittercproject.TwitterClient;
import com.codepath.apps.twittercproject.fragments.UserTimelineFragment;
import com.codepath.apps.twittercproject.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {

    private User user;
    @BindView(R.id.tbProfile)
    Toolbar tbProfile;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvTagline)
    TextView tvTagline;
    @BindView(R.id.tvFollowers)
    TextView tvFollowers;
    @BindView(R.id.tvFollowing)
    TextView tvFollowing;
    @BindView(R.id.ivProfilePic)
    ImageView ivProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setSupportActionBar(tbProfile);
        final Context context = this;
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Getting user data...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.show();
        String screenName = getIntent().getStringExtra("screen_name");
        TwitterClient twitterClient = TwitterApplication.getRestClient();
        twitterClient.getUserInfo(screenName, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                getSupportActionBar().setTitle(String.format("@ %s", user.getScreenName()));
                populateHeader(user);
                pd.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(context, "Unable to fetch user info", Toast.LENGTH_LONG).show();
                pd.dismiss();
                finish();
            }
        });
        if (savedInstanceState == null) {
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, userTimelineFragment);
            ft.commit();
        }
    }

    public void showUserProfile(View view) {

    }

    private void populateHeader(User user) {
        tvName.setText(user.getName());
        tvTagline.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFollowingCount() + " Following");
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfilePic);
    }
}
