package com.codepath.apps.restclienttemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.databinding.ActivityTimelineBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "TimelineActivity";
    private final int REQUEST_CODE = 20;
    private long MAX_ID;

    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener mScrollListener;

    TwitterClient mClient;
    RecyclerView mRvTweets;
    List<Tweet> mTweets;
    TweetsAdapter mAdapter;
    MenuItem mMiActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTimelineBinding binding = ActivityTimelineBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mClient = TwitterApp.getRestClient(this);
        // Find the recycler view

        // Initialize the list of tweets anda adapter
        mTweets = new ArrayList<>();
        mAdapter = new TweetsAdapter(this, mTweets);
        mRvTweets = binding.rvTweets;
        // Recycler view setup: layout manager and the adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvTweets.setLayoutManager(linearLayoutManager);
        mRvTweets.setAdapter(mAdapter);
        populateHomeTimeline();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync(0);
            }
        });

        mScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }
        };

        binding.rvTweets.addOnScrollListener(mScrollListener);
    }

    private void loadNextDataFromApi(int offset) {
        showProgressBar();
        mClient.getHomeTimelineExtend(MAX_ID, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                hideProgressBar();
                Log.i(TAG, "success endless scroll");
                JSONArray jsonArray = json.jsonArray;
                try {
                    mTweets.addAll(Tweet.fromJSONArray(jsonArray));
                    MAX_ID = mTweets.get(mTweets.size() - 1).getmId();
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "endless scroll fail" + response, throwable);
            }
        });
    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
        showProgressBar();
        mClient.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                hideProgressBar();
                JSONArray jsonArray = json.jsonArray;
                mAdapter.clear();
                try {
                    mAdapter.addAll(Tweet.fromJSONArray(jsonArray));
//                    adapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.compose) {
            // Compose item has been selected
            Toast.makeText(this, "Compose!", Toast.LENGTH_SHORT).show();
            // Navigate to compose activity
            Intent intent = new Intent(this, ComposeActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
            return true;
        }
        if (item.getItemId() == R.id.logout) {
            // Compose item has been selected
            Toast.makeText(this, "Logged out of account", Toast.LENGTH_SHORT).show();
            // Navigate to compose activity
            onLogoutButton();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showProgressBar() {
        // Show progress item
        mMiActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        mMiActionProgressItem.setVisible(false);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        mMiActionProgressItem = menu.findItem(R.id.miActionProgress);

        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            showProgressBar();
            // Get data from the intent (tweet)
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            // Update the RV with the tweet
            // Modify data source of tweets
            mTweets.add(0, tweet);
            // Update the adapter
            mAdapter.notifyItemInserted(0);
            mRvTweets.smoothScrollToPosition(0);
            hideProgressBar();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onLogoutButton() {
        mClient.clearAccessToken(); // forget who's logged in
        finish(); // navigate backwards to Login screen
    }

    private void populateHomeTimeline() {
        mClient.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess!" + json);
                JSONArray jsonArray = json.jsonArray;
                try {
                    mTweets.addAll(Tweet.fromJSONArray(jsonArray));
                    MAX_ID = (mTweets.get(mTweets.size() - 1)).getmId();
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "Json exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure!" + response, throwable);

            }
        });
    }

}