package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.databinding.ActivityTweetDetailBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import okhttp3.Headers;

public class TweetDetailActivity extends AppCompatActivity {

    public static final String TAG = "TwitterDetailActivity";
    public static final String TWEET = "tweet";

    TwitterClient client;
    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityTweetDetailBinding binding = ActivityTweetDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        client = TwitterApp.getRestClient(this);

        Intent intent = getIntent();
        tweet = Parcels.unwrap(intent.getParcelableExtra(TWEET));

        binding.tvUsername.setText(tweet.mUser.mName);
        binding.tvTweet.setText(tweet.mBody);
        binding.tvDetailName.setText("@" + tweet.mUser.mScreenName);
        Glide.with(this).load(tweet.mUser.mProfileImageURL).into(binding.ivProfilePic);

        if(tweet.mFavorite){
            binding.ibHeart.setImageResource(R.drawable.ic_vector_heart);
            binding.ibHeart.setTag(R.drawable.ic_vector_heart);
        } else {
            binding.ibHeart.setImageResource(R.drawable.ic_vector_heart_stroke);
            binding.ibHeart.setTag(R.drawable.ic_vector_heart_stroke);
        }

        binding.ibHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tweet is already liked; want to unlike
                if ((int) binding.ibHeart.getTag() == R.drawable.ic_vector_heart){
                    client.unlike(tweet.mId, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            // Unlike tweet
                            Log.i(TAG, "Liked tweet");
                            Toast.makeText(TweetDetailActivity.this, "Unliked", Toast.LENGTH_SHORT).show();
                            binding.ibHeart.setImageResource(R.drawable.ic_vector_heart_stroke);
                            binding.ibHeart.setTag(R.drawable.ic_vector_heart_stroke);
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "Failed to unlike tweet" + response, throwable);
                        }
                    });
                } else client.like(tweet.mId, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Toast.makeText(TweetDetailActivity.this, "Liked", Toast.LENGTH_SHORT).show();
                        binding.ibHeart.setImageResource(R.drawable.ic_vector_heart);
                        binding.ibHeart.setTag(R.drawable.ic_vector_heart);
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "Failed to like tweet" + response, throwable);
                    }
                });

                }
        });
    }

}