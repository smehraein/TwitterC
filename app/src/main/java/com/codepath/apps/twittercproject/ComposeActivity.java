package com.codepath.apps.twittercproject;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class ComposeActivity extends AppCompatActivity {

    @BindView(R.id.tbComposeTweet)
    Toolbar tbComposeTweet;
    @BindView(R.id.etTweet)
    EditText etTweet;
    @BindView(R.id.tvCharCount)
    TextView tvCharCount;

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
                finish();
            }
        });
        tvCharCount.setText("0 / 140");
    }

    @OnTextChanged(R.id.etTweet)
    public void updateCharCount(CharSequence charSequence) {
        tvCharCount.setText(String.format(Locale.US ,"%d / 140", etTweet.getText().length()));
    }
}
