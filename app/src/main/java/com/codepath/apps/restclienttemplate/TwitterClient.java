package com.codepath.apps.restclienttemplate;

import android.content.Context;

import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.FlickrApi;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/scribejava/scribejava/tree/master/scribejava-apis/src/main/java/com/github/scribejava/apis
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final BaseApi REST_API_INSTANCE = TwitterApi.instance(); // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = BuildConfig.CONSUMER_KEY;       // Change this inside apikey.properties
	public static final String REST_CONSUMER_SECRET = BuildConfig.CONSUMER_SECRET; // Change this inside apikey.properties
	public static final String HOME_TIMELINE_URL = "statuses/home_timeline.json";
	public static final String UPDATE_URL = "statuses/update.json";
	public static final String LIKE_URL = "favorites/create.json";
	public static final String UNLIKE_URL = "favorites/destroy.json";
	public static final String COUNT = "count";
	public static final String SINCE_ID = "since_id";
	public static final String MAX_ID = "max_id";
	public static final String ID = "id";
	public static final String STATUS = "status";

	// Landing page to indicate the OAuth flow worked in case Chrome for Android 25+ blocks navigation back to the app.
	public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

	// See https://developer.chrome.com/multidevice/android/intents
	public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

	public TwitterClient(Context context) {
		super(context, REST_API_INSTANCE,
				REST_URL,
				REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET,
				null,  // OAuth2 scope, null for OAuth1
				String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
						context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
	}
	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getHomeTimeline(JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl(HOME_TIMELINE_URL);
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put(COUNT, 25);
		params.put(SINCE_ID, 1);
		client.get(apiUrl, params, handler);
	}

	public void getHomeTimelineExtend(long max_id, JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl(HOME_TIMELINE_URL);
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put(COUNT, 25);
		params.put(MAX_ID, max_id);
		client.get(apiUrl, params, handler);
	}

	public void publishTweet(String tweetContent, JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl(UPDATE_URL);
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put(STATUS, tweetContent);
		client.post(apiUrl, params, "", handler);
	}

	public void like(long tweetId, JsonHttpResponseHandler handler){
		String apiUrl = getApiUrl(LIKE_URL);
		RequestParams params = new RequestParams();
		params.put(ID, tweetId);
		client.post(apiUrl, params, "", handler);
	}

	public void unlike(long tweetId, JsonHttpResponseHandler handler){
		String apiUrl = getApiUrl(UNLIKE_URL);
		RequestParams params = new RequestParams();
		params.put(ID, tweetId);
		client.post(apiUrl, params, "", handler);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}
