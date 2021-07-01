package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {

    public static final String NAME = "name";
    public static final String SCREEN_NAME = "screen_name";
    public static final String PROFILE_IMAGE_URL_HTTPS = "profile_image_url_https";

    public String mName;
    public String mScreenName;
    public String mProfileImageURL;

    // empty constructor needed by the Parceler Library
    public User() {}

    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.mName = jsonObject.getString(NAME);
        user.mScreenName = jsonObject.getString(SCREEN_NAME);
        user.mProfileImageURL = jsonObject.getString(PROFILE_IMAGE_URL_HTTPS);
        return user;
    }
}
