package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
public class Tweet {

    public static final String TEXT = "text";
    public static final String CREATED_AT = "created_at";
    public static final String USER = "user";
    public static final String ID = "id";
    public static final String FAVORITE = "favorited";
    public static final String ENTITIES = "entities";
    public static final String MEDIA = "media";
    public static final String MEDIA_URL_HTTPS = "media_url_https";

    public String mBody;
    public String mFullBody;
    public String mCreatedAt;
    public User mUser;
    public String mImage;
    public boolean mFavorite;
    public long mId;

    // empty constructor needed by Parcel library
    public Tweet(){}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.mBody = jsonObject.getString(TEXT);
        tweet.mCreatedAt = jsonObject.getString(CREATED_AT);
        tweet.mUser = User.fromJson(jsonObject.getJSONObject(USER));
        tweet.mId = jsonObject.getLong(ID);
        tweet.mFavorite = jsonObject.getBoolean(FAVORITE);
        JSONObject entities = jsonObject.getJSONObject(ENTITIES);
        if(entities.has(MEDIA)){

            tweet.mImage = entities.getJSONArray(MEDIA).getJSONObject(0).getString(MEDIA_URL_HTTPS);
        }
        else{
            tweet.mImage = null;
        }

        return tweet;
    }

    public long getmId() {
        return mId;
    }

    public static List<Tweet> fromJSONArray(JSONArray jsonArray) throws JSONException{
        List<Tweet> tweets = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
