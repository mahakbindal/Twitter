package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.w3c.dom.Text;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    Context mContext;
    List<Tweet> mTweets;
    // Pass in the context and list of tweets

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.mContext = context;
        this.mTweets = tweets;
    }

    // For each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data at position
        Tweet tweet = mTweets.get(position);
        // Bind the tweet with view holder
        holder.bind(tweet);

    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }


    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvTimestamp;
        ImageView ivMedia;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            ivMedia = itemView.findViewById(R.id.ivMedia);
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.mBody);
            tvScreenName.setText(tweet.mUser.mScreenName);
            Glide.with(mContext).load(tweet.mUser.mProfileImageURL).into(ivProfileImage);
            tvTimestamp.setText(tweet.getRelativeTimeAgo(tweet.mCreatedAt));
            if(tweet.mImage != null){
                ivMedia.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(tweet.mImage).into(ivMedia);
            }
            else{
                ivMedia.setVisibility(View.GONE);
            }
        }
    }
}
