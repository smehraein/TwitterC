package com.codepath.apps.twittercproject.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twittercproject.R;
import com.codepath.apps.twittercproject.TwitterApplication;
import com.codepath.apps.twittercproject.TwitterClient;
import com.codepath.apps.twittercproject.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    private TwitterClient client;
    @BindView(R.id.tbComposeTweet)
    Toolbar tbComposeTweet;
    @BindView(R.id.etTweet)
    EditText etTweet;
    @BindView(R.id.tvCharCount)
    TextView tvCharCount;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        ButterKnife.bind(this);
        setSupportActionBar(tbComposeTweet);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        tbComposeTweet.setNavigationIcon(R.drawable.ic_keyboard_backspace_white_24dp);
        tbComposeTweet.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        tvCharCount.setText("0 / 140");

        client = TwitterApplication.getRestClient();
    }

    @OnTextChanged(R.id.etTweet)
    public void updateCharCount(CharSequence charSequence) {
        tvCharCount.setText(String.format(Locale.US ,"%d / 140", etTweet.getText().length()));
    }

    @OnClick(R.id.btnSubmit)
    public void submitTweet(Button button) {
        String tweetBody = etTweet.getText().toString();
        if (!TextUtils.isEmpty(tweetBody)) {
            final Context context = this;
            final ProgressDialog pd = new ProgressDialog(context);
            pd.setTitle("Posting tweet...");
            pd.setMessage("Please wait.");
            pd.setCancelable(false);
            pd.show();
            client.postTweet(tweetBody, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Intent intent = new Intent();
                    Tweet tweet = Tweet.fromJSON(response, Tweet.TIMELINES_ENUM.HOME_TIMELINE);
                    intent.putExtra(Tweet.INTENT_TWEET, tweet);
                    setResult(RESULT_OK, intent);
                    finish();
                    pd.dismiss();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Toast.makeText(context, "Unable to post tweet.", Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
            });
        }
    }
}
