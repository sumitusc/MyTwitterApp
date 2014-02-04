package com.codepath.apps.mytwitterapp.fragments;

import org.json.JSONArray;

import android.os.Bundle;

import com.codepath.apps.mytwitterapp.MyTwitterApp;
import com.codepath.apps.mytwitterapp.constants.Constants;
import com.codepath.apps.mytwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TweetsListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle args = getArguments();
		String screenName = args.getString("screen_name");
		long userId = args.getLong("user_id",0);
		
		if(screenName != null && !screenName.isEmpty() && userId != 0){
			MyTwitterApp.getRestClient().getUserTimeline(new JsonHttpResponseHandler(){
				
				@Override
				public void onSuccess(JSONArray jsonTweets) {
					getAdapter().addAll(Tweet.fromJason(jsonTweets));
					//Log.d("DEBUG", "Timeline Tweets: "+jsonTweets.toString());
				}
			}, screenName, userId, Constants.MAX_COUNT_TWEETS_USER_TIMELINE);
		}else{
		
			MyTwitterApp.getRestClient().getUserTimeline(new JsonHttpResponseHandler(){
				
				@Override
				public void onSuccess(JSONArray jsonTweets) {
					getAdapter().addAll(Tweet.fromJason(jsonTweets));
					//Log.d("DEBUG", "Timeline Tweets: "+jsonTweets.toString());
				}
			}, Constants.MAX_COUNT_TWEETS_USER_TIMELINE);

		}
	}
}
