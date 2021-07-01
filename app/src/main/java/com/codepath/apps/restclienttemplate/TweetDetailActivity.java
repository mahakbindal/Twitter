package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.databinding.ActivityTweetDetailBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailActivity extends AppCompatActivity {

    TwitterClient client;
    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTweetDetailBinding binding = ActivityTweetDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        client = TwitterApp.getRestClient(this);

        Intent intent = getIntent();
        tweet = Parcels.unwrap(intent.getParcelableExtra("tweet"));

        binding.tvUsername.setText(tweet.mUser.mName);
        binding.tvTweet.setText(tweet.mBody);
        binding.tvDetailName.setText("@" + tweet.mUser.mScreenName);
        Glide.with(this).load(tweet.mUser.mProfileImageURL).into(binding.ivProfilePic);
    }
}